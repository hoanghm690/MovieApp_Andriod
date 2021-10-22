package com.example.movieapp.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.movieapp.R;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.adapters.MovieItemClickListener;
import com.example.movieapp.adapters.SliderPagerAdapter;
import com.example.movieapp.adapters.ViewPagerAdapter;
import com.example.movieapp.api.ApiUtils;
import com.example.movieapp.api.MovieService;
import com.example.movieapp.models.Phim;
import com.example.movieapp.models.ResponseParser;
import com.example.movieapp.ui.fragments.ActionFragment;
import com.example.movieapp.ui.fragments.ComedyFragment;
import com.example.movieapp.ui.fragments.DramaFragment;
import com.example.movieapp.ui.fragments.HomeFragment;
import com.example.movieapp.ui.fragments.WarFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieItemClickListener {
    List<Phim> listSlides;
    ViewPager sliderPager;
    ViewPager2 viewPager2;
    TabLayout indicator, tabLayout;
    RecyclerView moviesRV, moviesCovidRV;
    MovieService movieService;
    ViewPagerAdapter viewPagerAdapter;
    Call<ResponseParser> call;
    MovieAdapter movieAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("TH Play");
        movieService = ApiUtils.getMoiveService();

        iniViews();
        iniSlider();
        iniPopularMovies();
        iniCovidMovies();
        iniCategoryMovies();
    }

    private void iniViews() {
        sliderPager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);
        moviesRV = findViewById(R.id.Rv_movies_popular);
        moviesCovidRV = findViewById(R.id.Rv_movies_covid);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
    }

    private void iniCategoryMovies() {
        FragmentManager fm = getSupportFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter(fm,getLifecycle());
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("Hành động"));
        tabLayout.addTab(tabLayout.newTab().setText("Hoạt hình"));
        tabLayout.addTab(tabLayout.newTab().setText("Lãng mạn"));
        tabLayout.addTab(tabLayout.newTab().setText("Cổ trang"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    private void iniPopularMovies() {
        // Recyclerview Setup
        call = movieService.getListMovies("IwAR1k4WlQbyCdrKT7ITP-6RrfGhyIk-IFtByEE2uM_vBn_PWgXASG0mnaXF0");
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                ResponseParser responseParser = response.body();

                if (responseParser != null) {
                    List<Phim> listMovies = new ArrayList<>();
                    for (int i = 5; i < 15; i++) {
                        listMovies.add(responseParser.getPhim().get("phimle").get(i));
                    }
                    movieAdapter = new MovieAdapter(MainActivity.this, listMovies, MainActivity.this);
                    moviesRV.setAdapter(movieAdapter);
                    moviesRV.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<ResponseParser> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniCovidMovies() {
        // Recyclerview Setup
        call = movieService.getListMovies("IwAR1k4WlQbyCdrKT7ITP-6RrfGhyIk-IFtByEE2uM_vBn_PWgXASG0mnaXF0");
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                ResponseParser responseParser = response.body();

                if (responseParser != null) {
                    List<Phim> listMovies = new ArrayList<>();
                    for (int i = 15; i < 30; i++) {
                        listMovies.add(responseParser.getPhim().get("phimle").get(i));
                    }
                    movieAdapter = new MovieAdapter(MainActivity.this, listMovies, MainActivity.this);
                    moviesCovidRV.setAdapter(movieAdapter);
                    moviesCovidRV.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<ResponseParser> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniSlider() {
        call = movieService.getListMovies("IwAR1k4WlQbyCdrKT7ITP-6RrfGhyIk-IFtByEE2uM_vBn_PWgXASG0mnaXF0");
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                ResponseParser responseParser = response.body();

                if (responseParser != null) {
                    listSlides = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        listSlides.add(responseParser.getPhim().get("phimle").get(i));
                    }
                    SliderPagerAdapter adapter = new SliderPagerAdapter(MainActivity.this, listSlides);
                    sliderPager.setAdapter(adapter);

                    // setup timer
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
                    indicator.setupWithViewPager(sliderPager, true);
                }
            }

            @Override
            public void onFailure(Call<ResponseParser> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMovieClick(Phim movie, ImageView movieImageView) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("title", movie.getTitle());
        intent.putExtra("imgURl", movie.getImageUrl());
        intent.putExtra("category", movie.getCategory());
        startActivity(intent);

        Toast.makeText(this, "item clicked: " + movie.getTitle(), Toast.LENGTH_SHORT).show();
    }

    class SliderTimer extends TimerTask {
        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderPager.getCurrentItem() < listSlides.size()-1){
                        sliderPager.setCurrentItem(sliderPager.getCurrentItem()+1);
                    }
                    else {
                        sliderPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Tìm kiếm...");

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
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}