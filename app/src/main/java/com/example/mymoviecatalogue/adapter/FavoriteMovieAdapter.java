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
import com.example.mymoviecatalogue.database.MovieEntity;

import java.util.ArrayList;

public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.ViewHolder> {

    private Context context;

    public FavoriteMovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<MovieEntity> movies = new ArrayList<>();

    public void setMovies(ArrayList<MovieEntity> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteMovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMovieAdapter.ViewHolder holder, int position) {
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_animation));
        holder.imgPoster.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_animation));
        Glide.with(holder.itemView.getContext())
                .load(BuildConfig.POSTER_URL + movies.get(position).getMoviePoster())
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(holder.imgPoster);
        holder.tvTitle.setText(movies.get(position).getMovieTitle());
        holder.tvDate.setText(movies.get(position).getMovieDate());
        holder.tvRate.setText(movies.get(position).getMovieRate());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailMovieActivity.class);
            intent.putExtra(DetailMovieActivity.EXTRA_MOVIE, movies.get(position).getMovieId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tvDate, tvRate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();

            imgPoster = itemView.findViewById(R.id.img_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvRate = itemView.findViewById(R.id.tv_rate);
        }
    }
}
