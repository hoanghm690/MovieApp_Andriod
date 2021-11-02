package com.example.movieapp.Urls;

public class Urls {
    public static final String ROOT_URL = "http:/192.168.1.7/movies_app/";//Đổi theo localhost của máy
    public static final String REGISTER_URL = ROOT_URL+"register.php";
    public static final String LOGIN_URL = ROOT_URL+"login.php";
    public static final String RESET_PASSWORD_URL = ROOT_URL + "resetpassword.php";
    public static final String UPDATE_USER_URL = ROOT_URL + "update.php";
    public static final String UPDATE_USER_IMAGE_URL = ROOT_URL + "updateimg.php";
    public static final String INSERT_USER_MOVIE = ROOT_URL + "insert_user_movie.php";
    public static final String GET_USER_MOVIE = ROOT_URL + "get_user_movie.php";

    public static final String TOGGLE_BOOKMARK = ROOT_URL + "toggle_bookmark.php";
    public static final String CHECK_BOOKMARK = ROOT_URL + "check_bookmark.php";
    public static final String GET_BOOKMARK = ROOT_URL + "get_bookmark.php";

    public static final String API_PARAMS = "IwAR0miFArNvbJNtY5M7NCQA8654k8bwQ4klYV5Wwos0hs3Fqho9lYhVNSI5U";
}
