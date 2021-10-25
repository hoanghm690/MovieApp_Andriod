package com.example.movieapp.api;
import retrofit2.Call;
import com.example.movieapp.models.ResponseParser;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("db")
    Call<ResponseParser> getListMovies(@Query("fbclid") String fbclid);
}
