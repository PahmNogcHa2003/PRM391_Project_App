package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.viewholders.BindingViewHolder;
import com.example.prm391_project_apprestaurants.databinding.ViewholderReviewItemBinding;
import com.example.prm391_project_apprestaurants.entities.Review;

import java.util.List;

public class ReviewDashboardAdapter extends RecyclerView.Adapter<BindingViewHolder<ViewholderReviewItemBinding>>{
    private final List<Review> reviews;
    public ReviewDashboardAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }
    @NonNull
    @Override
    public BindingViewHolder<ViewholderReviewItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderReviewItemBinding binding = ViewholderReviewItemBinding.inflate(inflater, parent, false);
        return new BindingViewHolder<>(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<ViewholderReviewItemBinding> holder, int position) {
        Review review = reviews.get(position);
        holder.getBinding().setViewHolder(review);
        holder.getBinding().setImageRes(R.drawable.placeholder);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
