package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.admin.RestaurantDetailDashboardActivity;
import com.example.prm391_project_apprestaurants.controllers.admin.UpdateRestaurantDashboard;
import com.example.prm391_project_apprestaurants.controllers.viewholders.BindingViewHolder;
import com.example.prm391_project_apprestaurants.databinding.ViewholderRestaurantItemBinding;
import com.example.prm391_project_apprestaurants.entities.Restaurant;

import java.util.List;

public class RestaurantManagementAdapter extends RecyclerView.Adapter<BindingViewHolder<ViewholderRestaurantItemBinding>> {
    private final List<Restaurant> restaurants;
    public RestaurantManagementAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public BindingViewHolder<com.example.prm391_project_apprestaurants.databinding.ViewholderRestaurantItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderRestaurantItemBinding binding = ViewholderRestaurantItemBinding.inflate(inflater);
        return new BindingViewHolder<>(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<com.example.prm391_project_apprestaurants.databinding.ViewholderRestaurantItemBinding> holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        holder.getBinding().setViewHolder(restaurant);
        registerEvents(holder.getBinding(), position);
        holder.getBinding().executePendingBindings();
    }

    private void registerEvents(ViewholderRestaurantItemBinding binding, int position) {
        binding.cardViewRestaurant.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, RestaurantDetailDashboardActivity.class);
            intent.putExtra("restaurantId", restaurants.get(position).getId());
            context.startActivity(intent);
        });
        binding.buttonEdit.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, UpdateRestaurantDashboard.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants.clear();
        this.restaurants.addAll(restaurants);
        notifyDataSetChanged();
    }
}
