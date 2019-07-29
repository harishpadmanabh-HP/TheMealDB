package com.hp.themealdb.Retro;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retro {
    public Apis getApi()
    {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://www.themealdb.com/api/json/v1/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Apis apis=retrofit.create(Apis.class);
        return apis;
    }
}
