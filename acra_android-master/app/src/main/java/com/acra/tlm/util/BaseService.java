package com.acra.tlm.util;


import com.acra.tlm.common.AppConstant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseService {


    protected Retrofit mRetrofit;

    public abstract void cancelRequest();






    public BaseService() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // comment when going for Live
        httpClient.addInterceptor(httpLoggingInterceptor);

        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppConstant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }
}



