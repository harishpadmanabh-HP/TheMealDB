package com.hp.themealdb;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.harishpadmanabh.apppreferences.AppPreferences;
import com.hp.themealdb.Models.FilterByArea;
import com.hp.themealdb.Retro.Retro;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResult extends AppCompatActivity {

    private RecyclerView searchresultrecyclerview;

    String searchCategory,searchQuerry;
    AppPreferences appPreferences;
    Retro retro;
    List<FilterByArea.MealsBean> filterByAreaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initView();
        appPreferences = AppPreferences.getInstance(this, getResources().getString(R.string.app_name));

        retro=new Retro();

        searchCategory=appPreferences.getData("filtermeal");

        if(searchCategory.equals("area"))
        {
            searchQuerry=appPreferences.getData("searcharea");

            Call<FilterByArea> filterByAreaCall = retro.getApi().FILTER_BY_AREA_CALL(searchQuerry);
            filterByAreaCall.enqueue(new Callback<FilterByArea>() {
                @Override
                public void onResponse(Call<FilterByArea> call, Response<FilterByArea> response) {

                filterByAreaList=response.body().getMeals();
                    searchresultrecyclerview.setLayoutManager(new GridLayoutManager(SearchResult.this, 2));
                    searchresultrecyclerview.setAdapter(new SearchAdapter(filterByAreaList,SearchResult.this));




                }

                @Override
                public void onFailure(Call<FilterByArea> call, Throwable t) {

                    Toast.makeText(SearchResult.this, "FilterByArea failed : "+t, Toast.LENGTH_SHORT).show();

                }
            });



        }
        else if(searchCategory.equals("name"))
        {
            searchQuerry=appPreferences.getData("searchname");
        }
        else
        {
            Toast.makeText(this, "Invalid Search Querry.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(this,MainActivity.class));

        }


    }

    private void initView() {
        searchresultrecyclerview = (RecyclerView) findViewById(R.id.searchresultrecyclerview);
    }

    class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchVH>{

        List<FilterByArea.MealsBean> filterByAreaList;
        Context context;

        public SearchAdapter(List<FilterByArea.MealsBean> filterByAreaList, Context context) {
            this.filterByAreaList = filterByAreaList;
            this.context = context;
        }



        @NonNull
        @Override
        public SearchVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View searchview=LayoutInflater.from(parent.getContext()).inflate(R.layout.singlesearchitem, parent, false);

            return new SearchVH(searchview);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchVH holder, int position) {


//            Glide
//                    .with(context)
//                    .load(filterByAreaList.get(position).getStrMealThumb().trim())
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
                    .load(filterByAreaList.get(position).getStrMealThumb().trim())
                    // .load(BASE_POSTER_PATH+model.getPoster_path().trim())
                    .apply(options)
                    .into(new BitmapImageViewTarget(holder.mealimg) {
                        @Override
                        public void onResourceReady(Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                            super.onResourceReady(bitmap, transition);
                            Palette.from(bitmap).generate(palette -> setBackgroundColor(palette, holder));
                        }
                    });

            holder.mealname.setText(filterByAreaList.get(position).getStrMeal());

            holder.mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    appPreferences.saveData("searchmealname",filterByAreaList.get(position).getStrMeal());
                    appPreferences.saveData("searchmealid",filterByAreaList.get(position).getIdMeal());


                    startActivity(new Intent(SearchResult.this,SearchFilterScrolling.class));

                }
            });



        }
        private void setBackgroundColor(Palette palette, SearchVH holder ) {
            holder.mealname.setBackgroundColor(palette.getVibrantColor(SearchResult.this
                    .getResources().getColor(R.color.black_translucent_60)));
        }

        @Override
        public int getItemCount() {
            return filterByAreaList.size();
        }

        class SearchVH extends RecyclerView.ViewHolder{

            View mview;
            ImageView mealimg;
            TextView mealname;
            public SearchVH(@NonNull View itemView) {
                super(itemView);
                mview=itemView;
                mealimg=itemView.findViewById(R.id.searchresult_img);
                mealname=itemView.findViewById(R.id.searchresult_name);


            }
        }
    }
}
