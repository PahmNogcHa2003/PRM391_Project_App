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

/**
 * Adapter hiển thị danh sách nhà hàng, hỗ trợ đầy đủ các trường: tên, mô tả, địa chỉ, giá, số review,
 * số lượt yêu thích, điểm rating trung bình, trạng thái yêu thích.
 */
public class HomeRestaurantAdapter extends RecyclerView.Adapter<HomeRestaurantAdapter.ViewHolder> {

    private final Context context;
    private List<HomeRestaurant> restaurantList;
    private final OnItemClickListener listener;

    // Interface cho sự kiện click item và click yêu thích
    public interface OnItemClickListener {
        void onItemClick(HomeRestaurant restaurant);
        void onFavoriteClick(HomeRestaurant restaurant);
    }

    public HomeRestaurantAdapter(Context context, List<HomeRestaurant> restaurantList, OnItemClickListener listener) {
        this.context = context;
        this.restaurantList = restaurantList;
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
        HomeRestaurant restaurant = restaurantList.get(position);

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
            holder.imageFavorite.setImageResource(R.drawable.ic_favorite_filled); // icon trái tim đầy
        } else {
            holder.imageFavorite.setImageResource(R.drawable.ic_favorite_border); // icon trái tim viền
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
        return restaurantList != null ? restaurantList.size() : 0;
    }

    public void updateList(List<HomeRestaurant> newList) {
        this.restaurantList = newList;
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
