package com.example.movieapp.utils;

import android.util.Log;
import android.widget.Toast;

import com.example.movieapp.Urls.Urls;
import com.example.movieapp.api.ApiUtils;
import com.example.movieapp.api.MovieService;
import com.example.movieapp.models.Phim;
import com.example.movieapp.models.ResponseParser;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataSource {
    private MovieService movieService;
    public List<Phim> listMovies;
    public ResponseParser responseParser;

    public ResponseParser getMovies() {
        listMovies = new ArrayList<>();
        movieService = ApiUtils.getMoiveService();

        Call<ResponseParser> call = movieService.getListMovies(Urls.API_PARAMS);
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                responseParser = response.body();
            }

            @Override
            public void onFailure(Call<ResponseParser> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return responseParser;
    }
}
