package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.entities.Restaurant;

import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {
    private final List<Restaurant> suggestions;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Restaurant restaurant);
    }

    public SuggestionAdapter(List<Restaurant> suggestions, OnItemClickListener listener) {
        this.suggestions = suggestions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        Restaurant restaurant = suggestions.get(position);
        holder.tvName.setText(restaurant.getName());
        holder.tvCategory.setText(restaurant.getCategory());

        Glide.with(holder.itemView.getContext())
                .load(restaurant.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(holder.ivImage);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(restaurant));
    }

    @Override
    public int getItemCount() {
        return suggestions.size();
    }

    public static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage;
        TextView tvName, tvCategory;

        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivSuggestionImage);
            tvName = itemView.findViewById(R.id.tvSuggestionName);
            tvCategory = itemView.findViewById(R.id.tvSuggestionCategory);
        }
    }
}
