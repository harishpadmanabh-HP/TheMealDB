package com.hp.themealdb;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.harishpadmanabh.apppreferences.AppPreferences;
import com.hp.themealdb.Models.MealDetailModel;
import com.hp.themealdb.Retro.Retro;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFilterScrolling extends AppCompatActivity {

    private ImageView collapsingToolbarImageView;
    private TextView descsroll;
    AppPreferences appPreferences;
    Retro retro;
    List<MealDetailModel.MealsBean> detailModelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_filter_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
//
        initView();
        retro=new Retro();
        Call<MealDetailModel> mealDetailModelCall=retro.getApi().MEAL_DETAIL_MODEL_CALL(appPreferences.getData("searchmealname"));
        mealDetailModelCall.enqueue(new Callback<MealDetailModel>() {
            @Override
            public void onResponse(Call<MealDetailModel> call, Response<MealDetailModel> response) {
               detailModelList=response.body().getMeals();

               toolbarLayout.setTitle(detailModelList.get(0).getStrMeal());


                Glide
                    .with(SearchFilterScrolling.this)
                    .load(detailModelList.get(0).getStrMealThumb().trim())
                    //.load("https://www.themealdb.com/images/category/vegetarian.png")
                    .centerCrop()
                    // .placeholder(R.drawable.ic_launcher_foreground)
                    .into(collapsingToolbarImageView);

            descsroll.setText(detailModelList.get(0).getStrMeal()+"\n\n\n"+detailModelList.get(0).getStrInstructions());

            }

            @Override
            public void onFailure(Call<MealDetailModel> call, Throwable t) {

            }
        });

    }

    private void initView() {
        collapsingToolbarImageView = (ImageView) findViewById(R.id.collapsing_toolbar_image_view);
        descsroll = (TextView) findViewById(R.id.descsroll);
    }
}
