package com.example.movieapp.api;

public class ApiUtils {

    // Link api: https://api.apify.com/v2/key-value-stores/QubTry45OOCkTyohU/records/LATEST?fbclid=IwAR1k4WlQbyCdrKT7ITP-6RrfGhyIk-IFtByEE2uM_vBn_PWgXASG0mnaXF0

    private ApiUtils(){
    };

    public static final String API_URL = "https://api.apify.com";

    public static MovieService getMoiveService(){
        return RetrofitClient.getClient(API_URL).create(MovieService.class);
    }

}
