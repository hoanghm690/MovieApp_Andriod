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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.Urls.Urls;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.adapters.MovieItemClickListener;
import com.example.movieapp.api.ApiUtils;
import com.example.movieapp.api.MovieService;
import com.example.movieapp.models.Phim;
import com.example.movieapp.models.ResponseParser;

import java.util.ArrayList;
import java.util.List;

import gun0912.tedbottompicker.GridSpacingItemDecoration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements MovieItemClickListener{
    SearchView searchView;
    MovieAdapter movieAdapter;
    MovieService movieService;
    RecyclerView rvListMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("TH Play");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212b36")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        movieService = ApiUtils.getMoiveService();
        rvListMovies = findViewById(R.id.rv_movies_search);
        iniListMovies();
    }

    private void iniListMovies() {
        Call<ResponseParser> call = movieService.getListMovies(Urls.API_PARAMS);
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                ResponseParser responseParser = response.body();

                if (responseParser != null) {
                    List<Phim> listMovies = new ArrayList<>();
                    for (int i = 0; i < responseParser.getPhim().get("phimle").size(); i++) {
                        listMovies.add(responseParser.getPhim().get("phimle").get(i));
                    }

                    movieAdapter = new MovieAdapter(SearchActivity.this, listMovies, SearchActivity.this);
                    rvListMovies.setAdapter(movieAdapter);
                    rvListMovies.addItemDecoration(new GridSpacingItemDecoration(2, 68, true));
                    rvListMovies.setLayoutManager(new GridLayoutManager(SearchActivity.this, 2));
//                    rvListMovies.setLayoutManager(new LinearLayoutManager(SearchActivity.this, RecyclerView.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<ResponseParser> call, Throwable t) {
                t.printStackTrace();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);



        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        MenuItem loginMenuItem = menu.findItem(R.id.action_login);
        MenuItem nameMenuItem = menu.findItem(R.id.action_name);
        MenuItem profileMenuItem = menu.findItem(R.id.action_profile);
        MenuItem myListMenuItem = menu.findItem(R.id.action_mylist);
        MenuItem logoutMenuItem = menu.findItem(R.id.action_logout);

        searchMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        loginMenuItem.setVisible(false);
        nameMenuItem.setVisible(false);
        profileMenuItem.setVisible(false);
        myListMenuItem.setVisible(false);
        logoutMenuItem.setVisible(false);

        searchMenuItem.expandActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Nhập tìm kiếm của bạn tại đây");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                movieAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home){
            finish();
            Intent intentSearch = new Intent(this, MainActivity.class);
            startActivity(intentSearch);
        }
        return true;
    }
}