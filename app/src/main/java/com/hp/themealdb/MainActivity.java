package com.hp.themealdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.harishpadmanabh.apppreferences.AppPreferences;
import com.hp.themealdb.Models.CategoryModel;
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
    AppPreferences appPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        categorylist=findViewById(R.id.categorylist);
        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));


        randomimg=findViewById(R.id.randommealimage);
        randommeal=findViewById(R.id.randommeal);

        randommealdesc=findViewById(R.id.randommealdesc);


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
                    randommeal.setText(randomMealModelList.get(r).getStrMeal());
                    randommealdesc.setText("Instructions : \n"+randomMealModelList.get(r).getStrInstructions());
                }


            }

            @Override
            public void onFailure(Call<RandomMealModel> call, Throwable t) {

            }
        });

    }

    public void refresh(View view) {
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

            Glide
                    .with(context)
                    .load(categoryModelList.get(position).getStrCategoryThumb().trim())
                    //.load("https://www.themealdb.com/images/category/vegetarian.png")
                    .centerCrop()
                  // .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.catimage);


            holder.catname.setText(categoryModelList.get(position).getStrCategory());


            holder.mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    appPreferences.saveData("category",categoryModelList.get(position).getStrCategory());
                    startActivity(new Intent(MainActivity.this,MealFilter.class));
                }
            });
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
}
