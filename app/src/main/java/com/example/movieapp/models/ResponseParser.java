package com.example.movieapp.models;

import java.util.Date;
import java.util.Map;

public class ResponseParser {
    private String sourceUrl;
    private Date lastUpdatedAtApify;
    private String author;
    private Buymecoffee buymecoffee;
    private Date lastUpdatedAtSource;
    private Map<String, Phim> phim;

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Date getLastUpdatedAtApify() {
        return lastUpdatedAtApify;
    }

    public void setLastUpdatedAtApify(Date lastUpdatedAtApify) {
        this.lastUpdatedAtApify = lastUpdatedAtApify;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Buymecoffee getBuymecoffee() {
        return buymecoffee;
    }

    public void setBuymecoffee(Buymecoffee buymecoffee) {
        this.buymecoffee = buymecoffee;
    }

    public Date getLastUpdatedAtSource() {
        return lastUpdatedAtSource;
    }

    public void setLastUpdatedAtSource(Date lastUpdatedAtSource) {
        this.lastUpdatedAtSource = lastUpdatedAtSource;
    }

    public Map<String, Phim> getPhim() {
        return phim;
    }

    public void setPhim(Map<String, Phim> phim) {
        this.phim = phim;
    }
}
