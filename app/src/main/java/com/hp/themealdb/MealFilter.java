package com.hp.themealdb;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.harishpadmanabh.apppreferences.AppPreferences;
import com.hp.themealdb.Models.CategoryFilterModel;
import com.hp.themealdb.Retro.Retro;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealFilter extends AppCompatActivity {
    AppPreferences appPreferences;
    RecyclerView recyclerView;
    List<CategoryFilterModel.MealsBean> categoryFilterModelList;
    ImageView mealcat;
    TextView mealcatname;
    CoordinatorLayout coordinatorLayout;


    private void setBackgroundColorForText(Palette palette ) {

     mealcatname.setBackgroundColor(palette.getDominantColor(MealFilter.this
                .getResources().getColor(R.color.black_translucent_60)));
        mealcat.setBackgroundColor(palette.getDominantColor(MealFilter.this
                .getResources().getColor(R.color.black_translucent_60)));

        coordinatorLayout.setBackgroundColor(palette.getDominantColor(MealFilter.this
                .getResources().getColor(R.color.black_translucent_60)));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_filter);

        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));

        coordinatorLayout=findViewById(R.id.lay);
        mealcat=findViewById(R.id.mealcat);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.HIGH);
        Glide.with(this)
                .asBitmap()
                .load(appPreferences.getData("categoryimageurl"))
                // .load(BASE_POSTER_PATH+model.getPoster_path().trim())
                .apply(options)
                .into(new BitmapImageViewTarget(mealcat) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        super.onResourceReady(bitmap, transition);
                        Palette.from(bitmap).generate(palette -> setBackgroundColorForText(palette));
                    }
                });
        String category=appPreferences.getData("category");

        mealcatname=findViewById(R.id.mealcatname);
        mealcatname.setText(category);


        recyclerView=findViewById(R.id.mealsrv);
        Retro retro=new Retro();
        Call<CategoryFilterModel> categoryFilterModelCall=retro.getApi().CATEGORY_FILTER_MODEL_CALL(category);
        categoryFilterModelCall.enqueue(new Callback<CategoryFilterModel>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onResponse(Call<CategoryFilterModel> call, Response<CategoryFilterModel> response) {
             categoryFilterModelList=response.body().getMeals();
                recyclerView.setLayoutManager(new GridLayoutManager(MealFilter.this, 2));
             recyclerView.setAdapter(new MealsAdapter(categoryFilterModelList,MealFilter.this));

            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Log.e("onScrollStateChanged", String.valueOf(newState));
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    Log.e("onScrollStateChanged", String.valueOf(dx)+"    "+dy);

                    mealcat.setVisibility(View.GONE);
                }
            });

            }

            @Override
            public void onFailure(Call<CategoryFilterModel> call, Throwable t) {

            }
        });
    }

    class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealsRVVH>{

        List<CategoryFilterModel.MealsBean> categoryFilterModelList;
        Context context;

        public MealsAdapter(List<CategoryFilterModel.MealsBean> categoryFilterModelList, Context context) {
            this.categoryFilterModelList = categoryFilterModelList;
            this.context = context;
        }

        @NonNull
        @Override
        public MealsRVVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.singlesearchitem, parent, false);

            return new MealsRVVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MealsRVVH holder, final int position) {

//            Glide
//                    .with(context)
//                    .load(categoryFilterModelList.get(position).getStrMealThumb().trim())
//                    //.load("https://www.themealdb.com/images/category/vegetarian.png")
//                    .centerCrop()
//                    // .placeholder(R.drawable.ic_launcher_foreground)
//                    .into(holder.mealimg);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .priority(Priority.HIGH);
            Glide.with(context)
                    .asBitmap()
                    .load(categoryFilterModelList.get(position).getStrMealThumb().trim())
                    // .load(BASE_POSTER_PATH+model.getPoster_path().trim())
                    .apply(options)
                    .into(new BitmapImageViewTarget(holder.mealimg) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            super.onResourceReady(bitmap, transition);
                            Palette.from(bitmap).generate(palette -> setBackgroundColor(palette, holder));
                        }
                    });





            holder.mealname.setText(categoryFilterModelList.get(position).getStrMeal());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appPreferences.saveData("mealname",categoryFilterModelList.get(position).getStrMeal());


                    startActivity(new Intent(MealFilter.this,MealDetails.class));
                }
            });


        }
        private void setBackgroundColor(Palette palette, MealsRVVH holder ) {
            holder.mealname.setBackgroundColor(palette.getVibrantColor(MealFilter.this
                    .getResources().getColor(R.color.black_translucent_60)));
        }


        @Override
        public int getItemCount() {
            return categoryFilterModelList.size();
        }

        class MealsRVVH extends RecyclerView.ViewHolder{

            View mView;
            ImageView mealimg;
            TextView mealname;
            public MealsRVVH(@NonNull View itemView) {
                super(itemView);
                mView=itemView;
                mealimg=itemView.findViewById(R.id.searchresult_img);
                mealname=itemView.findViewById(R.id.searchresult_name);

            }
        }
    }
}
