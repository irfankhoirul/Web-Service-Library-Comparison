package com.irfankhoirul.apps.webservicelibrarycomparison.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.irfankhoirul.apps.webservicelibrarycomparison.R;
import com.irfankhoirul.apps.webservicelibrarycomparison.adapter.MovieAdapter;
import com.irfankhoirul.apps.webservicelibrarycomparison.model.IMovie;
import com.irfankhoirul.apps.webservicelibrarycomparison.model.Movie;
import com.irfankhoirul.apps.webservicelibrarycomparison.model.MovieJsonObject;
import com.irfankhoirul.apps.webservicelibrarycomparison.util.Constants;
import com.irfankhoirul.apps.webservicelibrarycomparison.util.Logger;
import com.irfankhoirul.apps.webservicelibrarycomparison.util.Url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataFragment extends Fragment {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @BindView(R.id.rvMain)
    RecyclerView rvMain;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private List<Movie> movies = new ArrayList<>();
    private String library;
    private OnFragmentInteractionListener mListener;
    private Context context;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance(String param) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putString("param", param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            library = getArguments().getString("param");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_data, container, false);
        ButterKnife.bind(this, v);
        context = getContext();
        loadData();

        return v;
    }

    private void loadData() {
        if (library.equalsIgnoreCase(BaseTabLayoutFragment.LIBRARY_VOLLEY)) {
            long timeStart = new Date().getTime();
            Logger.v("Volley->TimeStart", String.valueOf(timeStart));
            doVolley();
        } else if (library.equalsIgnoreCase(BaseTabLayoutFragment.LIBRARY_OKHTTP)) {
            long timeStart = new Date().getTime();
            Logger.v("OkHTTP->TimeStart", String.valueOf(timeStart));
            doOkHTTP();
        } else if (library.equalsIgnoreCase(BaseTabLayoutFragment.LIBRARY_RETROFIT)) {
            long timeStart = new Date().getTime();
            Logger.v("Retrofit->TimeStart", String.valueOf(timeStart));
            doRetrofit();
        }
    }

    private void doVolley() {
        /*
        * Volley
        * Load data dari url
        * */
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Url.POPULAR_MOVIE_COMPLETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*
                        * Parsing JSON
                        * */
                        long timeGetResponse = new Date().getTime();
                        Logger.v("Volley->TimeGetResponse", String.valueOf(timeGetResponse));
                        try {
                            JSONArray jsonMovieList = (new JSONObject(response)).getJSONArray("results");
                            Gson gson = new Gson();
                            Movie movie;
                            for (int i = 0; i < jsonMovieList.length(); i++) {
                                movie = gson.fromJson(jsonMovieList.get(i).toString(), Movie.class);
                                movies.add(movie);
                            }
                            long timeCompleteParsing = new Date().getTime();
                            Logger.v("Volley->TimeCompleteParsing", String.valueOf(timeCompleteParsing));
                            setupRecyclerView();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void doOkHTTP() {
        try {
            /*
            * OkHTTP
            * Load Data dari URL
            * */
            OkHttpClient client = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(Url.POPULAR_MOVIE_COMPLETE)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, okhttp3.Response response) throws IOException {
                    if (!response.isSuccessful())
                        throw new IOException("Unexpected code " + response);
                    else {
                        /*
                        * Parsing JSON
                        * */
                        long timeGetResponse = new Date().getTime();
                        Logger.v("OkHTTP->TimeGetResponse", String.valueOf(timeGetResponse));
                        try {
                            JSONArray jsonMovieList = (new JSONObject(response.body().string())).
                                    getJSONArray("results");
                            Gson gson = new Gson();
                            Movie movie;
                            for (int i = 0; i < jsonMovieList.length(); i++) {
                                movie = gson.fromJson(jsonMovieList.get(i).toString(), Movie.class);
                                movies.add(movie);
                            }
                            long timeCompleteParsing = new Date().getTime();
                            Logger.v("OkHTTP->TimeCompleteParsing", String.valueOf(timeCompleteParsing));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setupRecyclerView();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doRetrofit() {
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.POPULAR_MOVIE_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        IMovie movieService = retrofit.create(IMovie.class);

        retrofit2.Call<MovieJsonObject> call = movieService.popularMoviesJSON(
                Constants.SORT_BY_POPULARITY_DESCENDINF, "1", Constants.API_KEY);
        call.enqueue(new retrofit2.Callback<MovieJsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<MovieJsonObject> call,
                                   retrofit2.Response<MovieJsonObject> response) {
                movies = response.body().getResults();
                long timeCompleteParsing = new Date().getTime();
                Logger.v("Retrofit->TimeCompleteParsing", String.valueOf(timeCompleteParsing));
                setupRecyclerView();
            }

            @Override
            public void onFailure(retrofit2.Call<MovieJsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setupRecyclerView() {
        MovieAdapter mAdapter = new MovieAdapter(movies, context);
        rvMain.setLayoutManager(new GridLayoutManager(context, 2));
        rvMain.setItemAnimator(new DefaultItemAnimator());
        rvMain.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
