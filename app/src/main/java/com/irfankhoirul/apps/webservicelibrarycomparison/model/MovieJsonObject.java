package com.irfankhoirul.apps.webservicelibrarycomparison.model;

import java.util.List;

/**
 * Created by Irfan Khoirul on 07/05/2016.
 */
public class MovieJsonObject {
    private int page;
    private List<Movie> results;
    private int total_results;
    private int total_pages;

//    GETTER & SETTER

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }
}
