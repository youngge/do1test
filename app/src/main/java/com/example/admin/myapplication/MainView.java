package com.example.admin.myapplication;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by admin on 2017/9/6.
 */

public interface MainView {
    void getMovieOnSuccess(Call<MovieEntity> call, Response<MovieEntity> response);
    void getMovieOnFailed(Call<MovieEntity> call, Throwable t);
}
