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
import com.example.mymoviecatalogue.activity.DetailTVShowActivity;
import com.example.mymoviecatalogue.database.TvShowEntity;

import java.util.ArrayList;

public class FavoriteTVShowAdapter extends RecyclerView.Adapter<FavoriteTVShowAdapter.ViewHolder> {

    private Context context;

    public FavoriteTVShowAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<TvShowEntity> tvshows = new ArrayList<>();

    public void setTvShows(ArrayList<TvShowEntity> tvshows) {
        this.tvshows.clear();
        this.tvshows.addAll(tvshows);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteTVShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteTVShowAdapter.ViewHolder holder, int position) {
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_animation));
        holder.imgPoster.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_animation));
        Glide.with(holder.itemView.getContext())
                .load(BuildConfig.POSTER_URL + tvshows.get(position).getTvshowPoster())
                .into(holder.imgPoster);
        holder.tvTitle.setText(tvshows.get(position).getTvshowTitle());
        holder.tvDate.setText(tvshows.get(position).getTvshowDate());
        holder.tvRate.setText(tvshows.get(position).getTvshowRate());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailTVShowActivity.class);
            intent.putExtra(DetailTVShowActivity.EXTRA_TV_SHOW, tvshows.get(position).getTvshowId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tvshows.size();
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
