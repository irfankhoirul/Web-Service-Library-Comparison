package com.irfankhoirul.apps.webservicelibrarycomparison.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.irfankhoirul.apps.webservicelibrarycomparison.R;
import com.irfankhoirul.apps.webservicelibrarycomparison.model.Movie;
import com.irfankhoirul.apps.webservicelibrarycomparison.util.Url;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Irfan Khoirul on 06/05/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> moviesList;
    private Context context;

    public MovieAdapter(List<Movie> moviesList, Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int position) {
        final Movie movie = moviesList.get(position);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvPopularoty.setText(String.valueOf((int) movie.getPopularity()) + " %");
        holder.tvVoteCount.setText(String.valueOf(movie.getVote_count()) + " votes");


        /*
        * Downloading Image
        * */
        Picasso.with(context)
                .load(Url.IMAGE_URL + movie.getPoster_path())
                .placeholder(R.drawable.ic_broken_image)
                .error(R.drawable.ic_broken_image)
                .into(holder.imgPoster, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        /*
                        * Mengolah image yang telah didownload
                        * */
                        Bitmap bitmap = ((BitmapDrawable) holder.imgPoster.getDrawable()).getBitmap();
                        if (bitmap != null && !bitmap.isRecycled()) {
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    Palette.Swatch swatch = palette.getVibrantSwatch();
                                    if (swatch != null) {
                                        holder.llInformation.setBackgroundColor(swatch.getRgb());
                                        holder.tvVoteCount.setTextColor(swatch.getTitleTextColor());
                                        holder.tvGenre.setTextColor(swatch.getTitleTextColor());
                                        holder.tvPopularoty.setTextColor(swatch.getTitleTextColor());
                                        holder.tvTitle.setTextColor(swatch.getTitleTextColor());
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onError() {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cvItem)
        CardView cvItem;
        @BindView(R.id.imgPoster)
        ImageView imgPoster;
        @BindView(R.id.llInformation)
        LinearLayout llInformation;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvGenre)
        TextView tvGenre;
        @BindView(R.id.tvPopularoty)
        TextView tvPopularoty;
        @BindView(R.id.tvVoteCount)
        TextView tvVoteCount;


        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
