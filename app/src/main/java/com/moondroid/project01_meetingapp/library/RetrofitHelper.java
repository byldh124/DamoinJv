package com.moondroid.project01_meetingapp.library;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitHelper {
    private static String baseUrl = "http://moondroid.dothome.co.kr/";
    private static String urlForImg = "http://moondroid.dothome.co.kr/damoim/";

    public static Retrofit getRetrofitInstanceGson(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static Retrofit getRetrofitInstanceScalars(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl);
        builder.addConverterFactory(new NullOnEmptyConverterFactory());
        builder.addConverterFactory(ScalarsConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public static String getUrlForImg() {
        return urlForImg;
    }
}
