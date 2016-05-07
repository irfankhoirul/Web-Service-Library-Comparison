package com.irfankhoirul.apps.webservicelibrarycomparison.model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Irfan Khoirul on 07/05/2016.
 */
public interface IMovie {
//    @GET("movie?page={page}&sort_by={sort_by}&api_key={api_key}")
//    Call<List<Movie>> popularMoviesJSON(@Query("page") String page, @Query("sort_by") String sort_by, @Query("api_key") String api_key);

    @GET("movie")
    Call<MovieJsonObject> popularMoviesJSON(
            @Query("sort_by") String sort_by,
            @Query("page") String page,
            @Query("api_key") String api_key);
}
