package com.example.weatherapp;

import android.annotation.SuppressLint;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApi {
   static String base_url="https://api.openweathermap.org/data/2.5/";
     static Retrofit retrofit=null;

     static Retrofit getclient(){

       if(retrofit==null){

           retrofit=new Retrofit.Builder().
                   baseUrl(base_url).

                   addConverterFactory(GsonConverterFactory.create()).
                   build();



       }

           return retrofit;

    }


}
