package com.moondroid.project01_meetingapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {

    public static Retrofit getRetrofitInstanceGson(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(URLMngr.BASE_URL_DEFAULT);
        builder.addConverterFactory(NullOnEmptyConverterFactory.getInstance());
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static Retrofit getRetrofitInstanceGsonSetLenient(){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(URLMngr.BASE_URL_DEFAULT);
        builder.addConverterFactory(NullOnEmptyConverterFactory.getInstance());
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static Retrofit getRetrofit(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(URLMngr.BASE_URL_DEFAULT);
        builder.addConverterFactory(NullOnEmptyConverterFactory.getInstance());
        builder.addConverterFactory(ScalarsConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }
}
