package com.example.movieapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import com.example.movieapp.R;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView MovieImg;
    private TextView MovieTitle, MovieCategory;

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

        MovieTitle.setText(movieTitle);
        Picasso.with(this).load(movieImgURL).into(MovieImg);
        MovieCategory.setText(movieCate);
    }
}