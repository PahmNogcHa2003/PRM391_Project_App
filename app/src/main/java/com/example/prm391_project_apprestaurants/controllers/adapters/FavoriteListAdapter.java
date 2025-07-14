package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.entities.HomeRestaurant;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.ViewHolder> {

    private final Context context;
    private List<HomeRestaurant> favoriteList;
    private final OnItemClickListener listener;

    // Interface cho sự kiện click item
    public interface OnItemClickListener {
        void onItemClick(HomeRestaurant restaurant);
        void onFavoriteClick(HomeRestaurant restaurant);
    }

    public FavoriteListAdapter(Context context, List<HomeRestaurant> favoriteList, OnItemClickListener listener) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeRestaurant restaurant = favoriteList.get(position);

        holder.textName.setText(restaurant.getName());
        holder.textDescription.setText(restaurant.getDescription());
        holder.textAddress.setText(restaurant.getAddress() + " - " + restaurant.getDistrict());
        holder.textPrice.setText(restaurant.getPrice());
        holder.textReviewCount.setText(restaurant.getReviewCount() + " review(s)");
        holder.textFavoriteCount.setText(restaurant.getFavoriteCount() + " lượt thích");
        holder.textRating.setText("Rating: " + String.format("%.1f", restaurant.getRating()));

        // Load ảnh đại diện (nếu có link), nếu không thì dùng ảnh mặc định
        if (restaurant.getImageUrl() != null && !restaurant.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(restaurant.getImageUrl())
                    .placeholder(R.drawable.restaurant)
                    .into(holder.imageRestaurant);
        } else {
            holder.imageRestaurant.setImageResource(R.drawable.restaurant);
        }

        // Hiển thị icon yêu thích đúng trạng thái
        if (restaurant.isFavorite()) {
            holder.imageFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            holder.imageFavorite.setImageResource(R.drawable.ic_favorite_border);
        }

        // Click vào item để xem chi tiết
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(restaurant);
            }
        });

        // Click vào icon yêu thích để thêm/bỏ yêu thích
        holder.imageFavorite.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFavoriteClick(restaurant);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList != null ? favoriteList.size() : 0;
    }

    public void updateList(List<HomeRestaurant> newList) {
        this.favoriteList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageRestaurant, imageFavorite;
        TextView textName, textDescription, textAddress, textPrice, textReviewCount, textFavoriteCount, textRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageRestaurant = itemView.findViewById(R.id.imageRestaurant);
            imageFavorite = itemView.findViewById(R.id.imageFavorite);
            textName = itemView.findViewById(R.id.textName);
            textDescription = itemView.findViewById(R.id.textDescription);
            textAddress = itemView.findViewById(R.id.textAddress);
            textPrice = itemView.findViewById(R.id.textPrice);
            textReviewCount = itemView.findViewById(R.id.textReviewCount);
            textFavoriteCount = itemView.findViewById(R.id.textFavoriteCount);
            textRating = itemView.findViewById(R.id.textRating);
        }
    }
}
