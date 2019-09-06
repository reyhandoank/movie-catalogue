package com.example.mymoviecatalogue.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mymoviecatalogue.BuildConfig;
import com.example.mymoviecatalogue.R;
import com.example.mymoviecatalogue.activity.DetailMovieActivity;
import com.example.mymoviecatalogue.model.Movie;

import java.util.ArrayList;
import java.util.Objects;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<Movie> movies = new ArrayList<>();

    public void setMovies(ArrayList<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void refreshAdapter(ArrayList<Movie> movies) {
        this.movies.addAll(movies);
        notifyItemRangeChanged(0, this.movies.size());
    }

    public void clearTvShow() {
        movies.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, final int position) {

        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_animation));
        holder.imgPoster.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_animation));
        Glide.with(holder.itemView.getContext())
                .load(BuildConfig.POSTER_URL + movies.get(position).getPoster())
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(holder.imgPoster);
        holder.tvTitle.setText(movies.get(position).getTitle());
        holder.tvDate.setText(movies.get(position).getDate());
        holder.tvRate.setText(movies.get(position).getRate());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, Objects.requireNonNull(movies.get(position).getId()).toString());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPoster;
        TextView tvTitle, tvDate, tvRate, tvDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();

            imgPoster = itemView.findViewById(R.id.img_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvRate = itemView.findViewById(R.id.tv_rate);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }
}
