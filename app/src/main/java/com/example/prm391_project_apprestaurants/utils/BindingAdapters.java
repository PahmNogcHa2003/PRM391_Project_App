package com.example.prm391_project_apprestaurants.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.entities.ReviewStatistic;

import java.util.List;

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

    @BindingAdapter("imageDishUrl")
    public static void loadDishImage(ImageView view, String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(view.getContext())
                    .load(url)
                    .apply(RequestOptions.centerCropTransform())
                    .error(ContextCompat.getDrawable(view.getContext(), R.drawable.cutlery))
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

    @BindingAdapter(value = {"progressFromRatingList", "progressFromRatingValue"})
    public static void setProgressFromRating(ProgressBar progressBar, List<ReviewStatistic> list, int rating) {
        if (list != null) {
            for (ReviewStatistic rs : list) {
                if (rs.getRating() == rating) {
                    progressBar.setProgress((int) rs.getPercentage());
                    return;
                }
            }
        }
        progressBar.setProgress(0);
    }

    @BindingAdapter(value = {"percentageFromRatingList", "percentageFromRatingValue"})
    public static void setPercentFromRating(TextView percentage, List<ReviewStatistic> list, int rating) {
        if (list != null) {
            for (ReviewStatistic rs : list) {
                if (rs.getRating() == rating) {
                    percentage.setText(rs.getPercentage() + "%");
                    return;
                }
            }
        }
        percentage.setText("0%");
    }
}
