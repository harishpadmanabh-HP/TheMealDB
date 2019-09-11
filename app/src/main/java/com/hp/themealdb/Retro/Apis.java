package com.hp.themealdb.Retro;

import com.hp.themealdb.Models.CategoryFilterModel;
import com.hp.themealdb.Models.CategoryModel;
import com.hp.themealdb.Models.FilterByArea;
import com.hp.themealdb.Models.MealDetailModel;
import com.hp.themealdb.Models.RandomMealModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Apis {

    @GET("categories.php")
    Call<CategoryModel> CATEGORY_MODEL_CALL();

    @GET("random.php")
    Call<RandomMealModel> RANDOM_MEAL_MODEL_CALL();

    @GET("filter.php?")
    Call<CategoryFilterModel> CATEGORY_FILTER_MODEL_CALL(@Query("c") String category);

    @GET("search.php?")
    Call<MealDetailModel> MEAL_DETAIL_MODEL_CALL(@Query("s")String mealname);

    @GET("filter.php?")
    Call<FilterByArea> FILTER_BY_AREA_CALL(@Query("a")String area);




}
