package com.robinhood.wsc.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static String URL0 = "https://g";
    public static String URL1 = "uruapigamb.site/";
    public static String URL2 = "admin/api/trds3f2333/";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL0 + URL1 + URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
