package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.entities.Restaurant;

import java.util.List;

public class RestaurantManagementAdapter extends RecyclerView.Adapter<RestaurantManagementAdapter.ViewholderRestaurantItemBinding> {
    private final List<Restaurant> restaurants;
    public RestaurantManagementAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
    @NonNull
    @Override
    public ViewholderRestaurantItemBinding onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.viewholder_restaurant_item, parent, false);
        return new ViewholderRestaurantItemBinding(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewholderRestaurantItemBinding holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.setData(restaurant);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class ViewholderRestaurantItemBinding extends RecyclerView.ViewHolder {

        private TextView restaurantName;
        private TextView restaurantAddress;
        private TextView restaurantDescription;
        private ImageView restaurantImage;
        private TextView restaurantPrice;

        private ImageView mapIcon;

        public ViewholderRestaurantItemBinding(@NonNull View itemView) {
            super(itemView);
            bindingView();
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        public void bindingView() {
            restaurantAddress = itemView.findViewById(R.id.textAddress);
            restaurantDescription = itemView.findViewById(R.id.textDescription);
            restaurantImage = itemView.findViewById(R.id.imageRestaurant);
            restaurantName = itemView.findViewById(R.id.textName);
            restaurantPrice = itemView.findViewById(R.id.textPrice);
            mapIcon = itemView.findViewById(R.id.imageView8);
            mapIcon.setImageDrawable(itemView.getResources().getDrawable(R.drawable.placeholder));
        }

        public void setData(Restaurant restaurant) {
            restaurantName.setText(restaurant.getName());
            restaurantAddress.setText(restaurant.getAddress());
            restaurantDescription.setText(restaurant.getDescription());
            restaurantPrice.setText(restaurant.getPriceRange());
        }
    }
}
