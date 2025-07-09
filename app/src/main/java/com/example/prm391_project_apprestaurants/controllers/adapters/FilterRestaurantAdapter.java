package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.viewholders.BindingViewHolder;
import com.example.prm391_project_apprestaurants.databinding.ViewholderRestaurantItemBinding;
import com.example.prm391_project_apprestaurants.entities.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class FilterRestaurantAdapter extends RestaurantManagementAdapter {
    private List<Restaurant> localRestaurants;

    public FilterRestaurantAdapter(List<Restaurant> restaurants) {
        super(new ArrayList<>()); // Initialize parent with empty list
        this.localRestaurants = new ArrayList<>(restaurants != null ? restaurants : new ArrayList<>());
        Log.d("FilterRestaurantAdapter", "Initialized with " + (localRestaurants != null ? localRestaurants.size() : 0) + " restaurants");
        if (localRestaurants.isEmpty()) {
            Log.w("FilterRestaurantAdapter", "Warning: Initial restaurant list is empty!");
        }
    }

    @NonNull
    @Override
    public BindingViewHolder<ViewholderRestaurantItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderRestaurantItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.viewholder_restaurant_item, parent, false);
        if (binding == null) {
            Log.e("FilterRestaurantAdapter", "Binding inflation failed!");
        } else {
            Log.d("FilterRestaurantAdapter", "Created ViewHolder for position");
        }
        return new BindingViewHolder<>(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<ViewholderRestaurantItemBinding> holder, int position) {
        if (position >= 0 && position < localRestaurants.size()) {
            Restaurant restaurant = localRestaurants.get(position);
            if (restaurant != null) {
                holder.getBinding().setViewHolder(restaurant);
                Log.d("FilterRestaurantAdapter", "Binding restaurant: " + restaurant.getName() + ", Image: " + restaurant.getImage());
            } else {
                Log.e("FilterRestaurantAdapter", "Restaurant at position " + position + " is null!");
            }
        } else {
            Log.e("FilterRestaurantAdapter", "Invalid position: " + position + ", size: " + localRestaurants.size());
        }
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        int count = localRestaurants != null ? localRestaurants.size() : 0;
        Log.d("FilterRestaurantAdapter", "Item count: " + count);
        return count;
    }

    public void updateRestaurants(List<Restaurant> restaurants) {
        this.localRestaurants.clear();
        if (restaurants != null) {
            this.localRestaurants.addAll(restaurants);
        }
        Log.d("FilterRestaurantAdapter", "Updated with " + localRestaurants.size() + " restaurants");
        if (localRestaurants.isEmpty()) {
            Log.w("FilterRestaurantAdapter", "Updated list is empty!");
        }
        notifyDataSetChanged();
    }
}