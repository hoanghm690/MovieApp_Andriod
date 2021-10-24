package com.example.movieapp.models;

import java.util.List;

public class Phim {
    public String category;
    public List<Episode> episode;
    public String imageUrl;
    public String title;
    public String url;

    public Phim() {
    }

    public Phim(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public Phim(String category, String imageUrl, String title, String url) {
        this.category = category;
        this.imageUrl = imageUrl;
        this.title = title;
        this.url = url;
    }

    public Phim(String category, List<Episode> episode, String imageUrl, String title, String url) {
        this.category = category;
        this.episode = episode;
        this.imageUrl = imageUrl;
        this.title = title;
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Episode> getEpisode() {
        return episode;
    }

    public void setEpisode(List<Episode> episode) {
        this.episode = episode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
