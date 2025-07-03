package com.example.prm391_project_apprestaurants.controllers.viewholders;

import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BindingViewHolder<T> extends RecyclerView.ViewHolder {
    private final T binding;

    public BindingViewHolder(View itemView) {
        super(itemView);
        binding = (T) DataBindingUtil.bind(itemView);
    }
    public T getBinding() {
        return binding;
    }
}
