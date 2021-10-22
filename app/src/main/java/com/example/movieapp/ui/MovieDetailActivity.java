package com.example.movieapp.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.movieapp.R;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView MovieImg;
    private TextView MovieTitle, MovieCategory;
//    private Toolbar toolbarMovieDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //get data
        String movieTitle = getIntent().getExtras().getString("title");
        String movieImgURL = getIntent().getExtras().getString("imgURL");
        String movieCate = getIntent().getExtras().getString("category");

        MovieImg = findViewById(R.id.detail_movie_img);
        MovieTitle = findViewById(R.id.detail_movie_title);
        MovieCategory = findViewById(R.id.detail_movie_gerne);
//        toolbarMovieDetail = findViewById(R.id.toolbar_movie_detail);

        MovieTitle.setText(movieTitle);
        Picasso.with(this).load(movieImgURL).into(MovieImg);
        MovieCategory.setText(movieCate);

//        ActionToolbar();
    }

//    private void ActionToolbar() {
//        toolbarMovieDetail.setTitle("Chi tiết phim");
//        setSupportActionBar(toolbarMovieDetail);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbarMovieDetail.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
}