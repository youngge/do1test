package com.example.admin.myapplication;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by admin on 2017/9/7.
 */

public class MainPresenter {

    MainView mainView;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    public void getMovie(String baseURL) {
        if (mainView==null){
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);
        Call<MovieEntity> call = movieService.getTopMovie(0, 10);
        call.enqueue(new Callback<MovieEntity>() {
            @Override
            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
                mainView.getMovieOnSuccess(call, response);
            }

            @Override
            public void onFailure(Call<MovieEntity> call, Throwable t) {
                mainView.getMovieOnFailed(call, t);
            }
        });
    }

}
