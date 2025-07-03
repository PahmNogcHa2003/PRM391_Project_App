package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.prm391_project_apprestaurants.R;
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
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }
}
