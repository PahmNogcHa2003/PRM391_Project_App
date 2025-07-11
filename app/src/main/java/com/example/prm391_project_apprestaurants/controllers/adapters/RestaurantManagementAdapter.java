package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.controllers.admin.RestaurantDetailDashboardActivity;
import com.example.prm391_project_apprestaurants.controllers.admin.RestaurantManagementActivity;
import com.example.prm391_project_apprestaurants.controllers.admin.UpdateRestaurantDashboardActivity;
import com.example.prm391_project_apprestaurants.controllers.viewholders.BindingViewHolder;
import com.example.prm391_project_apprestaurants.databinding.ViewholderRestaurantItemBinding;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.services.RestaurantService;
import com.example.prm391_project_apprestaurants.utils.UtilHelper;

import java.util.List;

public class RestaurantManagementAdapter extends RecyclerView.Adapter<BindingViewHolder<ViewholderRestaurantItemBinding>> {
    private final List<Restaurant> restaurants;
    public RestaurantManagementAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
    public RestaurantService restaurantService;

    @NonNull
    @Override
    public BindingViewHolder<com.example.prm391_project_apprestaurants.databinding.ViewholderRestaurantItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderRestaurantItemBinding binding = ViewholderRestaurantItemBinding.inflate(inflater);
        restaurantService = new RestaurantService(parent.getContext());
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
            Intent intent = new Intent(context, UpdateRestaurantDashboardActivity.class);
            intent.putExtra("restaurantId", restaurants.get(position).getId());
            context.startActivity(intent);
        });
        binding.buttonDelete.setOnClickListener(v -> {
            Context context = v.getContext();
            AlertDialog alertDialog = UtilHelper.createDialog(context, "Delete Restaurant", "Do you want to delete this restaurant?", "Cancel", "Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           boolean isDeleted = restaurantService.deleteRestaurant(restaurants.get(position));
                           if (isDeleted) {
                               RestaurantManagementActivity activity = (RestaurantManagementActivity) context;
                               activity.loadRestaurantData();
                               Toast.makeText(context, "Delete restaurant successfully", Toast.LENGTH_SHORT).show();
                           }else{
                               Toast.makeText(context, "Delete restaurant failed", Toast.LENGTH_SHORT).show();
                           }
                        }
                    }
            , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
            });
        binding.buttonActivate.setOnClickListener(v -> {
            Context context = v.getContext();
            AlertDialog alertDialog = UtilHelper.createDialog(context, "Active Restaurant", "Do you want to active this restaurant?", "Cancel", "Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            boolean isActive = restaurantService.activeRestaurant(restaurants.get(position));
                            if (isActive) {
                                RestaurantManagementActivity activity = (RestaurantManagementActivity) context;
                                activity.loadRestaurantData();
                                Toast.makeText(context, "Active restaurant successfully", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Active restaurant failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
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
