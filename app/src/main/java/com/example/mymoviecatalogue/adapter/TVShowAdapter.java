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
import com.example.mymoviecatalogue.model.TvShow;

import java.util.ArrayList;
import java.util.Objects;

public class TVShowAdapter extends RecyclerView.Adapter<TVShowAdapter.ViewHolder> {

    private Context context;

    public TVShowAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<TvShow> tvshows = new ArrayList<>();

    public void setTVShows(ArrayList<TvShow> tvshows) {
        this.tvshows.clear();
        this.tvshows.addAll(tvshows);
        notifyDataSetChanged();
    }

    public void refreshAdapter(ArrayList<TvShow> tvshows) {
        this.tvshows.addAll(tvshows);
        notifyItemRangeChanged(0, this.tvshows.size());
    }

    public void clearMovie() {
        tvshows.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TVShowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tv_show, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowAdapter.ViewHolder holder, final int position) {
        holder.itemView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_animation));
        holder.imgPoster.setAnimation(AnimationUtils.loadAnimation(context, R.anim.item_animation));
        Glide.with(holder.itemView.getContext())
                .load(BuildConfig.POSTER_URL + tvshows.get(position).getPoster())
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(holder.imgPoster);
        holder.tvTitle.setText(tvshows.get(position).getTitle());
        holder.tvDate.setText(tvshows.get(position).getDate());
        holder.tvRate.setText(tvshows.get(position).getRate());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailTVShowActivity.class);
            intent.putExtra(DetailTVShowActivity.EXTRA_TV_SHOW, Objects.requireNonNull(tvshows.get(position).getId()).toString());
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
