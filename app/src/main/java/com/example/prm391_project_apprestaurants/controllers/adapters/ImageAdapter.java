package com.example.prm391_project_apprestaurants.controllers.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final List<String> imageUrls;
    private final Context context;

    public ImageAdapter(List<String> imageUrls, Context context) {
        this.imageUrls = imageUrls;
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String imagePath = imageUrls.get(position);
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            Uri uri = Uri.fromFile(imageFile);
            holder.imgReview.setImageURI(uri);
        } else {
            holder.imgReview.setImageResource(R.drawable.baseline_image_not_supported_24);
        }
    }


    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imgReview;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imgReview = itemView.findViewById(R.id.imgReviewImage);
        }
    }
}
