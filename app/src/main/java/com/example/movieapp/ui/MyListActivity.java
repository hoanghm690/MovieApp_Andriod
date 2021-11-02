package com.example.movieapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.Urls.Urls;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.adapters.MovieItemClickListener;
import com.example.movieapp.adapters.MyListAdapter;
import com.example.movieapp.api.ApiUtils;
import com.example.movieapp.api.MovieService;
import com.example.movieapp.models.Phim;
import com.example.movieapp.models.ResponseParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gun0912.tedbottompicker.GridSpacingItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyListActivity extends AppCompatActivity implements MovieItemClickListener {

    MyListAdapter movieAdapter;
    MovieService movieService;
    RecyclerView rvListMovies;
    List<Phim> movies;
    Call<ResponseParser> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        getSupportActionBar().setTitle("Phim của tôi");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212b36")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movieService = ApiUtils.getMoiveService();
        rvListMovies = findViewById(R.id.rv_my_list);
        iniListMovies();
    }

    @Override
    protected void onStart() {
        super.onStart();
        iniListMovies();
    }

    private void iniListMovies() {
        SharedPreferences sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE);
        Integer userID = sharedPref.getInt("UserID", 0);

        StringRequest request = new StringRequest(Request.Method.POST, Urls.GET_BOOKMARK,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("myListMovies");
                        call = movieService.getListMovies(Urls.API_PARAMS);
                        call.enqueue(new Callback<ResponseParser>() {
                            @Override
                            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                                ResponseParser responseParser = response.body();

                                if (responseParser != null) {
                                    List<Phim> list = new ArrayList<>();
                                    movies = new ArrayList<>();
                                    movies = responseParser.getPhim().get("phimle");
                                    String titleMovie;
                                    JSONObject object;

                                    for (int i = 0; i < jsonArray.length(); i++){
                                        try {
                                            object = jsonArray.getJSONObject(i);
                                            titleMovie = object.getString("title_movie");

                                            for (int j = 0; j < movies.size(); j++) {
                                                if (movies.get(j).getTitle().contains(titleMovie)) {
                                                    list.add(movies.get(j));
                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    movieAdapter = new MyListAdapter(MyListActivity.this, list, MyListActivity.this);
                                    rvListMovies.setAdapter(movieAdapter);
                                    if(MyListActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                                        rvListMovies.setLayoutManager(new GridLayoutManager(MyListActivity.this, 2));
                                    }
                                    else{
                                        rvListMovies.setLayoutManager(new GridLayoutManager(MyListActivity.this, 3));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseParser> call, Throwable t) {
                                t.printStackTrace();
                            }
                        });

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }, null)
        {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("userID", String.valueOf(userID));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    @Override
    public void onMovieClick(Phim movie, ImageView movieImageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("imgURL", movie.getImageUrl());
        intent.putExtra("category", movie.getCategory());
        intent.putExtra("episode", movie.getEpisode().get(0).getUrl());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            finish();
        }
        return true;
    }
}