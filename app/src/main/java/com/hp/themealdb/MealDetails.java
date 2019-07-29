package com.hp.themealdb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.harishpadmanabh.apppreferences.AppPreferences;
import com.hp.themealdb.Models.MealDetailModel;
import com.hp.themealdb.Retro.Retro;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MealDetails extends AppCompatActivity {
    AppPreferences appPreferences;
    ImageView mealimg;
    TextView mealnametxtvw,mealsdesctxtvw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));

        mealimg=findViewById(R.id.mealdetailimg);
        mealnametxtvw=findViewById(R.id.mealdetailname);
        mealsdesctxtvw=findViewById(R.id.mealdetaildesc);

        String mealname=appPreferences.getData("mealname");
        loadMealDetails(mealname);


    }

    private void loadMealDetails(String mealname) {
        Retro retro=new Retro();
        Call<MealDetailModel> mealDetailModelCall=retro.getApi().MEAL_DETAIL_MODEL_CALL(mealname);
        mealDetailModelCall.enqueue(new Callback<MealDetailModel>() {
            @Override
            public void onResponse(Call<MealDetailModel> call, Response<MealDetailModel> response) {
                Glide

                        .with(MealDetails.this)
                        .load(response.body().getMeals().get(0).getStrMealThumb().trim())
                        //.load("https://www.themealdb.com/images/category/vegetarian.png")
                        .centerCrop()
                        // .placeholder(R.drawable.ic_launcher_foreground)
                        .into(mealimg);

                mealnametxtvw.setText(response.body().getMeals().get(0).getStrMeal());
                mealsdesctxtvw.setText(response.body().getMeals().get(0).getStrInstructions());
            }

            @Override
            public void onFailure(Call<MealDetailModel> call, Throwable t) {

            }
        });
    }
}
