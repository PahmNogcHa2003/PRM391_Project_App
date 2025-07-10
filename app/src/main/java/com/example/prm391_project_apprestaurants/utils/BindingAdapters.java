package com.example.prm391_project_apprestaurants.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.prm391_project_apprestaurants.R;

public class BindingAdapters {
    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(view.getContext())
                    .load(url)
                    .apply(RequestOptions.centerCropTransform())
                    .error(ContextCompat.getDrawable(view.getContext(), R.drawable.restaurant))
                    .into(view);
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.placeholder));
        }
    }
    @BindingAdapter("iconMapUrl")
    public static void loadIconMapUrl(ImageView view, Drawable url) {
        view.setImageDrawable(url);
    }

    @BindingAdapter("circleImageUrl")
    public static void loadIconMapUrlWithCircle(ImageView view, Drawable url) {
        Glide.with(view.getContext())
                .load(url)
                .transform(new CenterCrop(), new RoundedCorners(24))
                .into(view);
    }
}
