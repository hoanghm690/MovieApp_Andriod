package com.example.movieapp.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.movieapp.R;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.adapters.SliderPagerAdapter;
import com.example.movieapp.api.ApiUtils;
import com.example.movieapp.api.MovieService;
import com.example.movieapp.models.Phim;
import com.example.movieapp.models.ResponseParser;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    List<Phim> listSlides;
    ViewPager sliderPager;
    TabLayout indicator;
    RecyclerView moviesRV;
    MovieService movieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Ứng dụng xem phim");

        iniViews();
        iniSlider();
        iniPopularMovies();
    }

    private void iniViews() {
        sliderPager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);
        moviesRV = findViewById(R.id.Rv_movies);
    }

    private void iniPopularMovies() {
        // Recyclerview Setup
        List<Phim> listMovies = new ArrayList<>();
        listMovies.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60de6d7faa1bb691e514b6d4_poster-anh-la-mua-xuan-cua-em.jpg", "Anh Là Mùa Xuân Của Em"));
        listMovies.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60ddc1a2085d2983a0be88fa_poster-toi-da-lo-yeu-muc-tieu-truy-sat-cua-minh.jpg", "Tôi Đã Lỡ Yêu Mục Tiêu Truy Sát Của Mình"));
        listMovies.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60d96d5835f810953887a808_poster-meo-hay-cau-nguyen.jpg", "Meo, Hãy Cầu Nguyện"));
        listMovies.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60d6f009b1a6a7b00d2ceaf4_poster-tham-phan-ac-ma.jpg", "Thẩm Phán Ác Ma"));

        MovieAdapter movieAdapter = new MovieAdapter(this, listMovies);
        moviesRV.setAdapter(movieAdapter);
        moviesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//      moviesRV.setLayoutManager(new GridLayoutManager(this, 3 ));
    }

    private void iniSlider() {
        movieService = ApiUtils.getMoiveService();

        Call<ResponseParser> call = movieService.getListMovies("IwAR1k4WlQbyCdrKT7ITP-6RrfGhyIk-IFtByEE2uM_vBn_PWgXASG0mnaXF0");
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                ResponseParser responseParser = response.body();

                if (responseParser != null) {
                    listSlides = new ArrayList<Phim>();
                    listSlides.add(responseParser.getPhim().get("phimle").get(0));
                    listSlides.add(responseParser.getPhim().get("phimle").get(1));
                    listSlides.add(responseParser.getPhim().get("phimle").get(2));
                    listSlides.add(responseParser.getPhim().get("phimle").get(3));
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

//        listSlides = new ArrayList<>();
//        listSlides.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60de6d7faa1bb691e514b6d4_poster-anh-la-mua-xuan-cua-em.jpg", "Anh Là Mùa Xuân Của Em"));
//        listSlides.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60ddc1a2085d2983a0be88fa_poster-toi-da-lo-yeu-muc-tieu-truy-sat-cua-minh.jpg", "Tôi Đã Lỡ Yêu Mục Tiêu Truy Sát Của Mình"));
//        listSlides.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60d96d5835f810953887a808_poster-meo-hay-cau-nguyen.jpg", "Meo, Hãy Cầu Nguyện"));
//        listSlides.add(new Phim("https://www.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/60d6f009b1a6a7b00d2ceaf4_poster-tham-phan-ac-ma.jpg", "Thẩm Phán Ác Ma"));
//        SliderPagerAdapter adapter = new SliderPagerAdapter(this, listSlides);
//        sliderPager.setAdapter(adapter);
//
//        // setup timer
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);
//        indicator.setupWithViewPager(sliderPager, true);
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
}