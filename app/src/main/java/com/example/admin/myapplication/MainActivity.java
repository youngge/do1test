package com.example.admin.myapplication;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainView {

    private TextView mTextMessage;
    private ImageView mImageView;
    private String baseURL = "https://api.douban.com/v2/movie/";
    private String picurl = "http://cn.bing.com/az/hprichbg/rb/AvalancheCreek_ROW11173354624_1920x1080.jpg";

    private int x=0;
    private int y=0;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mImageView.setImageBitmap((Bitmap) msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT>=21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }


        mTextMessage = (TextView) findViewById(R.id.message);
        mImageView = (ImageView) findViewById(R.id.iv_test);

        MainPresenter mainPresenter = new MainPresenter(this);
        mainPresenter.getMovie(baseURL);

        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL(picurl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    InputStream is = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    Message m = Message.obtain();
                    m.obj = bitmap;
                    mHandler.sendMessage(m);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void getMovieOnSuccess(Call<MovieEntity> call, Response<MovieEntity> response) {
        List<MovieEntity.SubjectsBean> subjects = response.body().getSubjects();
        int length = subjects.size();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            MovieEntity.SubjectsBean subjectsBean = subjects.get(i);
            sb.append(subjectsBean.getTitle() + "-" + subjectsBean.getRating().getAverage() + "\n");
        }
        mTextMessage.setText("onResponse\n" + sb.toString());
        ObjectAnimator animator = ObjectAnimator.ofFloat(mTextMessage,"rotation",1f,0f,0.5f,0f,1f);
        animator.setDuration(6000);
        animator.start();

    }

    @Override
    public void getMovieOnFailed(Call<MovieEntity> call, Throwable t) {
        mTextMessage.setText("onFailure\n" + t.getMessage());
    }

    public void scrollto(View view) {
        x+=20;
        y+=20;
        mImageView.scrollTo(20,20);
    }

    public void scrollby(View view) {
        mImageView.scrollBy(20,20);
    }
}
