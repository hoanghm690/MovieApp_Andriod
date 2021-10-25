package com.example.movieapp.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.movieapp.R;
import com.example.movieapp.Urls.Urls;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.adapters.MovieItemClickListener;
import com.example.movieapp.adapters.ViewPagerAdapter;
import com.example.movieapp.api.ApiUtils;
import com.example.movieapp.api.MovieService;
import com.example.movieapp.models.Phim;
import com.example.movieapp.models.ResponseParser;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity implements MovieItemClickListener {

    private ImageView MovieImg, MovieCoverImg;
    private TextView MovieTitle, MovieCategory;
    private RecyclerView RvMoiveRelated;
    private MovieAdapter movieAdapter;
    private MovieService movieService;
    private Call<ResponseParser> call;
    private List<Phim> listMovies, movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212b36")));

        movieService = ApiUtils.getMoiveService();

        iniMovieDetail();
        iniMovieRelated();
    }

    void iniMovieDetail() {
        //get data
        String movieTitle = getIntent().getExtras().getString("title");
        String movieImgURL = getIntent().getExtras().getString("imgURL");
        String movieCate = getIntent().getExtras().getString("category");

        MovieImg = findViewById(R.id.detail_movie_img);
        MovieTitle = findViewById(R.id.detail_movie_title);
        MovieCategory = findViewById(R.id.detail_movie_gerne);
        MovieCoverImg = findViewById(R.id.detail_movie_cover);
        RvMoiveRelated = findViewById(R.id.detail_related_movies);

        getSupportActionBar().setTitle(movieTitle);

        MovieTitle.setText(movieTitle);
        Picasso.with(this).load(movieImgURL).into(MovieImg);
        Picasso.with(this).load(movieImgURL).into(MovieCoverImg);
        MovieCategory.setText(movieCate);
    }

    void iniMovieRelated() {
        call = movieService.getListMovies(Urls.API_PARAMS);
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                ResponseParser responseParser = response.body();

                if (responseParser != null) {
                    listMovies = new ArrayList<>();
                    movies = new ArrayList<>();
                    movies = responseParser.getPhim().get("phimle");
                    String movieCate = getIntent().getExtras().getString("category");

                    for (int i = 0; i < movies.size(); i++) {
                        if (movies.get(i).getCategory().contains(movieCate)) {
                            listMovies.add(movies.get(i));
                        }
                    }
                    Log.v("list",""+listMovies);
                    movieAdapter = new MovieAdapter(MovieDetailActivity.this, listMovies,MovieDetailActivity.this);
                    RvMoiveRelated.setAdapter(movieAdapter);
                    RvMoiveRelated.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<ResponseParser> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MovieDetailActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMovieClick(Phim movie, ImageView movieImageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("imgURL", movie.getImageUrl());
        intent.putExtra("category", movie.getCategory());
        intent.putExtra("url", movie.getUrl());
        startActivity(intent);

        Toast.makeText(this, "item clicked: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }
}