package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.controllers.viewholders.BindingViewHolder;
import com.example.prm391_project_apprestaurants.databinding.ViewholderCategoryItemBinding;
import com.example.prm391_project_apprestaurants.entities.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<BindingViewHolder<ViewholderCategoryItemBinding>> {
    private final List<Category> categories;
    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @NonNull
    @Override
    public BindingViewHolder<ViewholderCategoryItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderCategoryItemBinding binding = ViewholderCategoryItemBinding.inflate(inflater, parent, false);
        return new BindingViewHolder<>(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<ViewholderCategoryItemBinding> holder, int position) {
        Category category = categories.get(position);
        holder.getBinding().setViewHolder(category);
        holder.getBinding().checkBox.setText(category.getName());
        holder.getBinding().checkBox.setChecked(category.isSelected());
        holder.getBinding().checkBox.setOnClickListener(v -> {
            if (holder.getBinding().checkBox.isChecked()) {
                category.setSelected(true);
            } else {
                category.setSelected(false);
            }
        });
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
