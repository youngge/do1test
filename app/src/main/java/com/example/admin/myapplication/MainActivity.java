package com.example.admin.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements MainView {

    private TextView mTextMessage;
    private String baseURL = "https://api.douban.com/v2/movie/d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);

        getMovie();

    }

    private void getMovie() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);
        Call<MovieEntity> call = movieService.getTopMovie(0, 10);
        call.enqueue(new Callback<MovieEntity>() {
            @Override
            public void onResponse(Call<MovieEntity> call, Response<MovieEntity> response) {
                List<MovieEntity.SubjectsBean> subjects = response.body().getSubjects();
                int length = subjects.size();
                StringBuffer sb = new StringBuffer();
                for (int i=0;i<length;i++){
                    MovieEntity.SubjectsBean subjectsBean = subjects.get(i);
                    sb.append(subjectsBean.getTitle()+subjectsBean.getRating().getAverage()+"\n");
                }
                mTextMessage.setText("onResponse\n"+sb.toString());
            }

            @Override
            public void onFailure(Call<MovieEntity> call, Throwable t) {
                mTextMessage.setText("onFailure\n"+t.getMessage());
            }
        });
    }

    @Override
    public void getMovieOnSuccess(Call<MovieEntity> call, Response<MovieEntity> response) {
        List<MovieEntity.SubjectsBean> subjects = response.body().getSubjects();
        int length = subjects.size();
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<length;i++){
            MovieEntity.SubjectsBean subjectsBean = subjects.get(i);
            sb.append(subjectsBean.getTitle()+subjectsBean.getRating().getAverage()+"\n");
        }
        mTextMessage.setText("onResponse\n"+sb.toString());
    }

    @Override
    public void getMovieOnFailed(Call<MovieEntity> call, Throwable t) {
        mTextMessage.setText("onFailure\n"+t.getMessage());
    }
}
