package com.robinhood.wsc.data;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("getInfo")
    Call<EntityAppInfo> getInfo(@Query("geo") String geo, @Query("bundle") String bundle, @Query("naming") String naming);
}
