package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;

import java.util.List;

public class PreviewImageAdapter extends RecyclerView.Adapter<PreviewImageAdapter.ViewHolder> {
    private List<Uri> imageUris;
    private Context context;

    public PreviewImageAdapter(Context context, List<Uri> imageUris) {
        this.context = context;
        this.imageUris = imageUris;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imgReview.setImageURI(imageUris.get(position));
    }

    @Override
    public int getItemCount() {
        return imageUris.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgReview;
        ViewHolder(View view) {
            super(view);
            imgReview = view.findViewById(R.id.imgReviewImage);
        }
    }
}

