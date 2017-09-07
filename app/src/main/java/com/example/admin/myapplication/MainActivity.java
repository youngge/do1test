package com.example.admin.myapplication;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.ColorFilterTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import jp.wasabeef.glide.transformations.gpu.ToonFilterTransformation;
import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MainView {

    private TextView mTextMessage;
    private ImageView mImageView;
    private String baseURL = "https://api.douban.com/v2/movie/";
    private String picurl = "http://cn.bing.com/az/hprichbg/rb/AvalancheCreek_ROW11173354624_1920x1080.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        mImageView = (ImageView) findViewById(R.id.iv_test);

        MainPresenter mainPresenter = new MainPresenter(this);
        mainPresenter.getMovie(baseURL);

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
        Glide.with(this)
                .load(picurl)
                .bitmapTransform(new BlurTransformation(this,5)
                        , new RoundedCornersTransformation(this, 40, 20))
                .into(mImageView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("title")
                .setMessage("message")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Snackbar.make(mTextMessage,"ok",Snackbar.LENGTH_SHORT).show();
                    }
                })
                .create()
                .show();
    }

    @Override
    public void getMovieOnFailed(Call<MovieEntity> call, Throwable t) {
        mTextMessage.setText("onFailure\n" + t.getMessage());
    }
}
