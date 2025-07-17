package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.controllers.admin.UpdateUserDashboardActivity;
import com.example.prm391_project_apprestaurants.controllers.admin.UserDetailDashboardActivity;
import com.example.prm391_project_apprestaurants.controllers.viewholders.BindingViewHolder;
import com.example.prm391_project_apprestaurants.databinding.ViewholderUserItemBinding;
import com.example.prm391_project_apprestaurants.entities.User;

import java.util.List;

public class UserManagementAdapter extends RecyclerView.Adapter<BindingViewHolder<ViewholderUserItemBinding>> {
    private List<User> users;
    public UserManagementAdapter(List<User> users) {
        this.users = users;
    }
    @NonNull
    @Override
    public BindingViewHolder<ViewholderUserItemBinding> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewholderUserItemBinding binding = ViewholderUserItemBinding.inflate(inflater, parent, false);
        return new BindingViewHolder<>(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull BindingViewHolder<ViewholderUserItemBinding> holder, int position) {
        User user = users.get(position);
        holder.getBinding().cardViewUser.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), UserDetailDashboardActivity.class);
            intent.putExtra("userId", user.getId());
            v.getContext().startActivity(intent);
            ((Activity) v.getContext()).finish();
        });
        holder.getBinding().buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), UpdateUserDashboardActivity.class);
            intent.putExtra("userId", user.getId());
            v.getContext().startActivity(intent);
            ((Activity) v.getContext()).finish();
        });
        holder.getBinding().setViewHolder(user);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setUsers(List<User> users) {
        this.users.clear();
        this.users.addAll(users);
        notifyDataSetChanged();
    }
}
