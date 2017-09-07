package com.example.admin.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by admin on 2017/9/6.
 */

public interface MovieService {

    @GET("top250")
    Call<MovieEntity> getTopMovie(@Query("start")int start,@Query("count")int count);
}
