package com.hp.themealdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_filter);

        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));

        recyclerView=findViewById(R.id.mealsrv);
        Retro retro=new Retro();
        String category=appPreferences.getData("category");
        Call<CategoryFilterModel> categoryFilterModelCall=retro.getApi().CATEGORY_FILTER_MODEL_CALL(category);
        categoryFilterModelCall.enqueue(new Callback<CategoryFilterModel>() {
            @Override
            public void onResponse(Call<CategoryFilterModel> call, Response<CategoryFilterModel> response) {
             categoryFilterModelList=response.body().getMeals();
             recyclerView.setLayoutManager(new LinearLayoutManager(MealFilter.this,RecyclerView.VERTICAL,false));
             recyclerView.setAdapter(new MealsAdapter(categoryFilterModelList,MealFilter.this));


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
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.singlemealitem, parent, false);

            return new MealsRVVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MealsRVVH holder, final int position) {

            Glide
                    .with(context)
                    .load(categoryFilterModelList.get(position).getStrMealThumb().trim())
                    //.load("https://www.themealdb.com/images/category/vegetarian.png")
                    .centerCrop()
                    // .placeholder(R.drawable.ic_launcher_foreground)
                    .into(holder.mealimg);
            holder.mealname.setText(categoryFilterModelList.get(position).getStrMeal());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    appPreferences.saveData("mealname",categoryFilterModelList.get(position).getStrMeal());


                    startActivity(new Intent(MealFilter.this,MealDetails.class));
                }
            });


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
                mealimg=itemView.findViewById(R.id.mealimg);
                mealname=itemView.findViewById(R.id.mealname);

            }
        }
    }
}
