package com.moondroid.project01_meetingapp.network;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class NullOnEmptyConverterFactory extends Converter.Factory {

    private static NullOnEmptyConverterFactory mInstance;

    private NullOnEmptyConverterFactory() {
        super();
    }

    public static synchronized NullOnEmptyConverterFactory getInstance(){
        if (mInstance == null){
            mInstance = new NullOnEmptyConverterFactory();
        }
        return mInstance;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit)
    {
        final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
        return new Converter<ResponseBody, Object>() {
            @Override
            public Object convert(ResponseBody body) throws IOException
            {
                if (body.contentLength() == 0) {
                    return null;
                }
                return delegate.convert(body);
            }
        };
    }
}
