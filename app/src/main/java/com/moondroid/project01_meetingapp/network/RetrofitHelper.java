package com.moondroid.project01_meetingapp.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {
    private final static String BASE_URL = "http://moondroid.dothome.co.kr/";
    private final static String URL_FOR_IMG = "http://moondroid.dothome.co.kr/damoim/";

    public static Retrofit getRetrofitInstanceGson(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static Retrofit getRetrofitInstanceGsonSetLenient(){
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static Retrofit getRetrofitInstanceScalars(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BASE_URL);
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(ScalarsConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getUrlForImg() {
        return URL_FOR_IMG;
    }
}
