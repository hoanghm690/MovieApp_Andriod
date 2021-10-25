package com.example.movieapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.movieapp.R;
import com.example.movieapp.Urls.Urls;
import com.example.movieapp.adapters.MovieAdapter;
import com.example.movieapp.adapters.MovieItemClickListener;
import com.example.movieapp.adapters.ViewPagerAdapter;
import com.example.movieapp.api.ApiUtils;
import com.example.movieapp.api.MovieService;
import com.example.movieapp.models.Phim;
import com.example.movieapp.models.ResponseParser;
import com.example.movieapp.ui.MainActivity;
import com.example.movieapp.ui.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActionFragment extends Fragment {

    RecyclerView recyclerView;
    List<Phim> dataHolder;
    List<Phim> movies;

    MovieService movieService;
    Call<ResponseParser> call;
    MovieAdapter movieAdapter;

    public ActionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movieService = ApiUtils.getMoiveService();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_action, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAction);

        iniMoviesAction();

        return view;
    }

    void iniMoviesAction() {
        call = movieService.getListMovies(Urls.API_PARAMS);
        call.enqueue(new Callback<ResponseParser>() {
            @Override
            public void onResponse(Call<ResponseParser> call, Response<ResponseParser> response) {
                ResponseParser responseParser = response.body();

                if (responseParser != null) {
                    dataHolder = new ArrayList<>();
                    movies = new ArrayList<>();
                    movies = responseParser.getPhim().get("phimle");
                    for (int i = 0; i < movies.size(); i++) {
                        if (movies.get(i).getCategory().contains("Phim hành động")) {
                            dataHolder.add(movies.get(i));
                        }
                    }
                    movieAdapter = new MovieAdapter(getActivity(), dataHolder, (MovieItemClickListener) getActivity());
                    recyclerView.setAdapter(movieAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                }
            }

            @Override
            public void onFailure(Call<ResponseParser> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}