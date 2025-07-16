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
import com.example.prm391_project_apprestaurants.entities.Menu;
import java.util.List;

public class FeaturedMenuAdapter extends RecyclerView.Adapter<FeaturedMenuAdapter.MenuViewHolder> {

    private final Context context;
    private final List<Menu> menus;

    public FeaturedMenuAdapter(Context context, List<Menu> menus) {
        this.context = context;
        this.menus = menus;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_featured_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Menu menu = menus.get(position);
        holder.tvDishName.setText(menu.getDishName());
        String formattedPrice = formatPrice(menu.getPrice());
        holder.tvPrice.setText(formattedPrice);
        Glide.with(context)
                .load(menu.getImageUrl())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.ivDishImage);
    }
    private String formatPrice(String price) {
        try {
            // Nếu price đã có định dạng đúng (ví dụ: "65000" -> "65.000 VND")
            double amount = Double.parseDouble(price);
            return String.format("%,.0f VND", amount).replace(",", ".");
        } catch (NumberFormatException e) {
            // Nếu price đã có định dạng sẵn
            return price.contains("VND") ? price : price + " VND";
        }
    }
    @Override
    public int getItemCount() {
        return menus.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDishImage;
        TextView tvDishName, tvPrice;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDishImage = itemView.findViewById(R.id.ivDishImage);
            tvDishName = itemView.findViewById(R.id.tvDishName);

            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}