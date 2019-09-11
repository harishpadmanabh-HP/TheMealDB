package com.hp.themealdb.Retro;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {
    // create a method which returns our api interface class
    public Apis getApi()
    {
        //build retrofit object
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //connect api class with this builder

        Apis apis=retrofit.create(Apis.class);
        return apis;
    }
}
