package com.irfankhoirul.apps.webservicelibrarycomparison.util;

/**
 * Created by Irfan Khoirul on 06/05/2016.
 */
public class Url {
    public static String POPULAR_MOVIE_BASE = "https://api.themoviedb.org/3/discover/";
    public static String POPULAR_MOVIE_COMPLETE = POPULAR_MOVIE_BASE + "movie?page=1&sort_by=" +
            Constants.SORT_BY_POPULARITY_DESCENDINF + "&api_key=" + Constants.API_KEY;
    public static String IMAGE_URL = "http://image.tmdb.org/t/p/w500/";
}
