package com.example.movieapp.models;

import java.util.List;

public class Phim {
    public List<Phimbo> phimbo;
    public List<Phimbo> phimle;

    public List<Phimbo> getPhimbo() {
        return phimbo;
    }

    public void setPhimbo(List<Phimbo> phimbo) {
        this.phimbo = phimbo;
    }

    public List<Phimbo> getPhimle() {
        return phimle;
    }

    public void setPhimle(List<Phimbo> phimle) {
        this.phimle = phimle;
    }
}
