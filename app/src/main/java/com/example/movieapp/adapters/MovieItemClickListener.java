package com.example.movieapp.adapters;

import android.widget.ImageView;

import com.example.movieapp.models.Phim;

public interface MovieItemClickListener {
    void onMovieClick(Phim movie, ImageView movieImageView);
}
