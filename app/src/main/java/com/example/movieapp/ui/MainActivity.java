package com.example.movieapp.ui;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.movieapp.R;
import com.example.movieapp.Urls.Urls;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.adapters.MovieItemClickListener;
import com.example.movieapp.adapters.SliderPagerAdapter;
import com.example.movieapp.adapters.ViewPagerAdapter;
import com.example.movieapp.api.ApiUtils;
import com.example.movieapp.api.MovieService;
import com.example.movieapp.models.Phim;
import com.example.movieapp.models.ResponseParser;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieItemClickListener {
    List<Phim> listSlides,movies;
    ArrayList<Phim> callApiListMovies;
    ViewPager sliderPager;
    ViewPager2 viewPager2;
    TabLayout indicator, tabLayout;
    RecyclerView moviesRV, moviesCovidRV, moviesProposeRV;
    MovieService movieService;
    ViewPagerAdapter viewPagerAdapter;
    Call<ResponseParser> call;
    MovieAdapter movieAdapter;
    SearchView searchView;
    TextView txtPropose;

    MenuItem itemLogin;
    MenuItem itemProfile;
    MenuItem itemLogout;
    MenuItem itemName;
    MenuItem itemMyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("TH Play");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212b36")));

        movieService = ApiUtils.getMoiveService();

        callApi();

        Log.v("callApiListMovies",""+callApiListMovies);

        iniViews();
        iniSlider();
        iniPopularMovies();
        iniCovidMovies();
        iniCategoryMovies();
        iniProposeMovies();
    }

    private void iniViews() {
        sliderPager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);
        moviesRV = findViewById(R.id.Rv_movies_popular);
        moviesCovidRV = findViewById(R.id.Rv_movies_covid);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        moviesProposeRV = findViewById(R.id.Rv_movies_propose);
        txtPropose = findViewById(R.id.txtPropose);
        moviesProposeRV = findViewById(R.id.Rv_movies_propose);
    }

    private void iniProposeMovies() {
        SharedPreferences sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE);
        Integer userID = sharedPref.getInt("UserID", 1);

        StringRequest request = new StringRequest(Request.Method.POST, Urls.GET_USER_MOVIE,
            response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("historyMovies");

                    call = movieService.getListMovies(Urls.API_PARAMS);
                    call.enqueue(new Callback<ResponseParser>() {
                        @Override
                        public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                            ResponseParser responseParser = response.body();

                            if (responseParser != null) {
                                List<Phim> list = new ArrayList<>();
                                movies = new ArrayList<>();
                                movies = responseParser.getPhim().get("phimle");
                                String categoryMovie;
                                JSONObject object;

                                for (int i = 0; i < jsonArray.length(); i++){
                                    try {
                                        Integer flag = 0;
                                        object = jsonArray.getJSONObject(i);
                                        categoryMovie = object.getString("category_movie");

                                        for (int j = 0; j < movies.size(); j++) {
                                            if (movies.get(j).getCategory().contains(categoryMovie)) {
                                                list.add(movies.get(j));
                                                flag++;
                                            }

                                            if (flag == 5) {
                                                break;
                                            }
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                movieAdapter = new MovieAdapter(MainActivity.this, list, MainActivity.this);
                                moviesProposeRV.setAdapter(movieAdapter);
                                moviesProposeRV.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseParser> call, Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
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

    private void iniCategoryMovies() {
        FragmentManager fm = getSupportFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter(fm,getLifecycle());
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addTab(tabLayout.newTab().setText("Hành động"));
        tabLayout.addTab(tabLayout.newTab().setText("Hoạt hình"));
        tabLayout.addTab(tabLayout.newTab().setText("Tình cảm"));
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

    public void callApi () {
        callApiListMovies = new ArrayList<>();
        call = movieService.getListMovies(Urls.API_PARAMS);
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                ResponseParser responseParser = response.body();

                if (responseParser != null) {
                    callApiListMovies.addAll(responseParser.getPhim().get("phimle"));
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
        call = movieService.getListMovies(Urls.API_PARAMS);
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                ResponseParser responseParser = response.body();

                if (responseParser != null) {
                    listSlides = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        listSlides.add(responseParser.getPhim().get("phimle").get(i));
                    }

                    SliderPagerAdapter adapter = new SliderPagerAdapter(MainActivity.this, listSlides, MainActivity.this);
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

    private void iniPopularMovies() {
        // Recyclerview Setup
        call = movieService.getListMovies(Urls.API_PARAMS);
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
        call = movieService.getListMovies(Urls.API_PARAMS);
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
        return true;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setQueryHint("Nhập tìm kiếm của bạn tại đây");
//
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intentSearch = new Intent(MainActivity.this, SearchActivity.class);
//                startActivity(intentSearch);
//            }
//        });
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                movieAdapter.getFilter().filter(query);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                movieAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (!searchView.isIconified()) {
//            searchView.setIconified(true);
//            return;
//        }
//        super.onBackPressed();
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                Intent intentSearch = new Intent(this, SearchActivity.class);
                startActivity(intentSearch);
                item.getActionView().setVisibility(View.GONE);
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                return true;

            case R.id.action_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_profile:
                Intent intentProfile = new Intent(this, ProfileActivity.class);
                startActivity(intentProfile);
                return true;

            case R.id.action_mylist:
                Intent intentMyList = new Intent(this, MyListActivity.class);
                startActivity(intentMyList);
                return true;

            case R.id.action_logout:
                SharedPreferences sharedPref = getSharedPreferences("User",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("UserName");
                editor.remove("UserEmail");
                editor.remove("isLogin");
                editor.remove("UserId");
                editor.commit();
                Intent intentMain = new Intent(this, MainActivity.class);
                startActivity(intentMain);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        itemLogin = menu.findItem(R.id.action_login);
        itemProfile = menu.findItem(R.id.action_profile);
        itemLogout = menu.findItem(R.id.action_logout);
        itemName = menu.findItem(R.id.action_name);
        itemMyList = menu.findItem(R.id.action_mylist);

        SharedPreferences sharedPref = getSharedPreferences("User",Context.MODE_PRIVATE);
        boolean wasLogin = sharedPref.getBoolean("isLogin",false);
        String username = sharedPref.getString("UserName",null);

        if(wasLogin){
            moviesProposeRV.setVisibility(View.VISIBLE);
            txtPropose.setVisibility(View.VISIBLE);
            itemLogin.setVisible(false);
            itemProfile.setVisible(true);
            itemLogout.setVisible(true);
            itemName.setVisible(true);
            itemName.setTitle("Xin chào, "+username);
            itemMyList.setVisible(true);
        }
        else {
            moviesProposeRV.setVisibility(View.GONE);
            txtPropose.setVisibility(View.GONE);
            itemLogin.setVisible(true);
            itemProfile.setVisible(false);
            itemLogout.setVisible(false);
            itemName.setVisible(false);
            itemMyList.setVisible(false);
        }
        return true;
    }
}