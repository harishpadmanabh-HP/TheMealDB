package com.hp.themealdb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.harishpadmanabh.apppreferences.AppPreferences;
import com.hp.themealdb.Models.CategoryModel;
import com.hp.themealdb.Models.FilterByArea;
import com.hp.themealdb.Models.RandomMealModel;
import com.hp.themealdb.Retro.Retro;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
RecyclerView categorylist;
    List<CategoryModel.CategoriesBean> categoryModelList;
    ImageView randomimg;
    TextView randommeal,randommealdesc;
    SearchView searchView;
    AppPreferences appPreferences;
    Boolean is_desc_visible=true;
    CollapsingToolbarLayout toolbarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categorylist=findViewById(R.id.categorylist);
        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        searchView=findViewById(R.id.searchView);
        randomimg=findViewById(R.id.randommealimage);
       // randommeal=findViewById(R.id.randommealimage);


        randommealdesc=findViewById(R.id.randommealdesc);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randommealdesc.setVisibility(View.INVISIBLE);
                is_desc_visible=false;

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {

                randommealdesc.setVisibility(View.INVISIBLE);
                is_desc_visible=false;

               // Toast.makeText(MainActivity.this, "onQueryTextSubmit"+s, Toast.LENGTH_SHORT).show();

                LayoutInflater inflat=LayoutInflater.from(MainActivity.this);
                final View cuslay=inflat.inflate(R.layout.alertradio,null);

                final RadioGroup radioGroup=cuslay.findViewById(R.id.radiogrp);
                Button search=cuslay.findViewById(R.id.searchalert);

                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setView(cuslay);
                final AlertDialog alert=builder.create();
                alert.show();
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int selectedId=radioGroup.getCheckedRadioButtonId();

//                         RadioButton  radioSexButton=(RadioButton)findViewById(selectedId);
//                        Toast.makeText(MainActivity.this,selectedId,Toast.LENGTH_SHORT).show();

                        RadioButton area=cuslay.findViewById(R.id.radioarea);
                        RadioButton name=cuslay.findViewById(R.id.radioname);

                        if(area.isChecked())
                        {
                            Toast.makeText(MainActivity.this, "area", Toast.LENGTH_SHORT).show();

                            appPreferences.removeKey("filtermeal");
                            appPreferences.saveData("filtermeal","area");
                            appPreferences.saveData("searcharea",s);
                            startActivity(new Intent(MainActivity.this,SearchResult.class));

                        }
                        else if (name.isChecked())
                        {
                            Toast.makeText(MainActivity.this, "name", Toast.LENGTH_SHORT).show();

                            appPreferences.removeKey("filtermeal");
                            appPreferences.saveData("filtermeal","name");
                            appPreferences.saveData("searchname",s);

                        }
                        else{
                            Toast.makeText(MainActivity.this, "nothing selected", Toast.LENGTH_SHORT).show();
                        }

                    }
                });



                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                randommealdesc.setVisibility(View.INVISIBLE);
                is_desc_visible=false;

                //for search with each letter
                // Toast.makeText(MainActivity.this, "onQueryTextChange"+s, Toast.LENGTH_SHORT).show();
                return false;
            }
        });




        Retro retro=new Retro();
        Call<CategoryModel> categoryModelCall=retro.getApi().CATEGORY_MODEL_CALL();
        categoryModelCall.enqueue(new Callback<CategoryModel>() {
            @Override
            public void onResponse(Call<CategoryModel> call, Response<CategoryModel> response) {
            categoryModelList=response.body().getCategories();

            for(int i=0;i<categoryModelList.size();i++)
            {
                Log.e("ThumbImage   : ",categoryModelList.get(i).getStrCategoryThumb());
                Log.e("catagoryname  : ",categoryModelList.get(i).getStrCategory());
            }
                LinearLayoutManager verticalLayoutmanager
                        = new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false);
                categorylist.setLayoutManager(verticalLayoutmanager);
                categorylist.setAdapter(new CategoryAdapter(categoryModelList,MainActivity.this));


            }

            @Override
            public void onFailure(Call<CategoryModel> call, Throwable t) {

            }
        });

        loadRandomMeals();



    }


    private void loadRandomMeals() {
        Retro retro=new Retro();
        Call<RandomMealModel> randomMealModelCall=retro.getApi().RANDOM_MEAL_MODEL_CALL();
        randomMealModelCall.enqueue(new Callback<RandomMealModel>() {
            @Override
            public void onResponse(Call<RandomMealModel> call, Response<RandomMealModel> response) {

                List<RandomMealModel.MealsBean> randomMealModelList=response.body().getMeals();

                for(int r=0;r<randomMealModelList.size();r++) {

                    Glide
                            .with(MainActivity.this)
                            .load(randomMealModelList.get(r).getStrMealThumb().trim())
                            //.load("https://www.themealdb.com/images/category/vegetarian.png")
                            .centerCrop()
                            // .placeholder(R.drawable.ic_launcher_foreground)
                            .into(randomimg);
                    toolbarLayout.setTitle(randomMealModelList.get(0).getStrMeal());

                    //randommeal.setText(randomMealModelList.get(r).getStrMeal());
                    randommealdesc.setText(randomMealModelList.get(r).getStrMeal()+"\n\n\n"+"Instructions : \n"+randomMealModelList.get(r).getStrInstructions());
                }


            }

            @Override
            public void onFailure(Call<RandomMealModel> call, Throwable t) {

            }
        });

    }

//    public void refresh(View view) {
//        loadRandomMeals();
//    }

    public void refreshm(View view) {
        loadRandomMeals();
    }

    class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryRVVH>{

        List<CategoryModel.CategoriesBean> categoryModelList;
Context context;

        public CategoryAdapter(List<CategoryModel.CategoriesBean> categoryModelList, Context context) {
            this.categoryModelList = categoryModelList;
            this.context = context;
        }

        @NonNull
        @Override
        public CategoryRVVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.singleitemcat, parent, false);

            return new CategoryRVVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryRVVH holder, final int position) {

//            Glide
//                    .with(context)
//                    .load(categoryModelList.get(position).getStrCategoryThumb().trim())
//                    //.load("https://www.themealdb.com/images/category/vegetarian.png")
//                    .centerCrop()
//                  // .placeholder(R.drawable.ic_launcher_foreground)
//                    .into(holder.catimage);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .priority(Priority.HIGH);
            Glide.with(context)
                    .asBitmap()
                    .load(categoryModelList.get(position).getStrCategoryThumb().trim())
                    // .load(BASE_POSTER_PATH+model.getPoster_path().trim())
                    .apply(options)
                    .into(new BitmapImageViewTarget(holder.catimage) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            super.onResourceReady(bitmap, transition);
                            Palette.from(bitmap).generate(palette -> setBackgroundColor(palette, holder));
                        }
                    });


            holder.catname.setText(categoryModelList.get(position).getStrCategory());




            holder.mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appPreferences.saveData("categoryimageurl",categoryModelList.get(position).getStrCategoryThumb().trim());

                    appPreferences.saveData("category",categoryModelList.get(position).getStrCategory());
                    startActivity(new Intent(MainActivity.this,MealFilter.class));
                }
            });
        }
        private void setBackgroundColor(Palette palette,CategoryRVVH holder ) {
            holder.catname.setTextColor(palette.getVibrantColor(MainActivity.this
                    .getResources().getColor(R.color.black_translucent_60)));
        }

        @Override
        public int getItemCount() {
            return categoryModelList.size();
        }

        class CategoryRVVH extends RecyclerView.ViewHolder{

            View mview;
            ImageView catimage;
            TextView catname;

            public CategoryRVVH(@NonNull View itemView) {
                super(itemView);
                mview=itemView;
                catimage=itemView.findViewById(R.id.singleimagecat);
                catname=itemView.findViewById(R.id.singletextname);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(!is_desc_visible)
            randommealdesc.setVisibility(View.VISIBLE);
        else if(is_desc_visible)
            super.onBackPressed();



    }
}
