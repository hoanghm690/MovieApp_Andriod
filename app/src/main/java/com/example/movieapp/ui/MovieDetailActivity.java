package com.example.movieapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.Urls.Urls;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.adapters.MovieItemClickListener;
import com.example.movieapp.adapters.ViewPagerAdapter;
import com.example.movieapp.api.ApiUtils;
import com.example.movieapp.api.MovieService;
import com.example.movieapp.models.Phim;
import com.example.movieapp.models.ResponseParser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private FloatingActionButton fab_player;
    private ToggleButton AddBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212b36")));

        movieService = ApiUtils.getMoiveService();

        iniMovieDetail();
        iniMovieRelated();

        fab_player = findViewById(R.id.fab_play_movie);
        fab_player.setOnClickListener(view -> UserMovie());

        AddBtn = findViewById(R.id.btn_save_my_list);
        CheckMyList();
        AddBtn.setOnClickListener(view -> AddMyList());
    }

    void CheckMyList() {
        SharedPreferences sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE);
        Integer userID = sharedPref.getInt("UserID", 1);
        String movieTitle = getIntent().getExtras().getString("title");

        StringRequest request = new StringRequest(Request.Method.POST, Urls.CHECK_BOOKMARK,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String result = jsonObject.getString("status");
                        if(result.equals("true")){
                            AddBtn.toggle();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }

                }, error -> {
            massage(error.getMessage());
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("userID", String.valueOf(userID));
                params.put("titleMovie",movieTitle);
                Log.d("messageeeeee", "getParams: "+params);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MovieDetailActivity.this);
        queue.add(request);
    }

    void AddMyList() {
        SharedPreferences sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE);
        Integer userID = sharedPref.getInt("UserID", 1);
        String movieTitle = getIntent().getExtras().getString("title");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.TOGGLE_BOOKMARK, response -> {}, error -> {}){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", String.valueOf(userID));
                params.put("titleMovie",movieTitle);
                Log.d("message", "getParams: "+params);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MovieDetailActivity.this);
        queue.add(stringRequest);

    }

    void UserMovie() {
        SharedPreferences sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE);
        Integer userID = sharedPref.getInt("UserID", 1);
        String movieCate = getIntent().getExtras().getString("category");
        String movieEpisode = getIntent().getExtras().getString("episode");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.INSERT_USER_MOVIE, response -> {}, error -> {}){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", String.valueOf(userID));
                params.put("categoryMovie",movieCate);
                Log.d("message", "getParams: "+params);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MovieDetailActivity.this);
        queue.add(stringRequest);

        Intent intent = new Intent(MovieDetailActivity.this, MoviePlayerActivity.class);
        intent.putExtra("episode", movieEpisode);
        startActivity(intent);
    }

    void iniMovieDetail() {
        //get data
        String movieTitle = getIntent().getExtras().getString("title");
        String movieImgURL = getIntent().getExtras().getString("imgURL");
        String movieCate = getIntent().getExtras().getString("category");
        String movieEpisode = getIntent().getExtras().getString("episode");

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
        intent.putExtra("episode", movie.getEpisode().get(0).getUrl());
        startActivity(intent);

        Toast.makeText(this, "item clicked: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            finish();
        }
        return true;
    }
    public void massage(String massage){
        Toast.makeText(this, massage, Toast.LENGTH_LONG).show();
    }
}