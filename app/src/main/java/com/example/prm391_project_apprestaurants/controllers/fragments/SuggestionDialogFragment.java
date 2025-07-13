package com.example.prm391_project_apprestaurants.controllers.fragments;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.entities.Restaurant;

public class SuggestionDialogFragment extends DialogFragment {
    private Restaurant restaurant;
    private OnActionListener listener;

    public interface OnActionListener {
        void onSuggestAgain();
        void onViewDetail(int restaurantId);
    }

    public static SuggestionDialogFragment newInstance(Restaurant res) {
        SuggestionDialogFragment frag = new SuggestionDialogFragment();
        frag.restaurant = res;
        return frag;
    }

    public void setOnActionListener(OnActionListener l) {
        listener = l;
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle sis) {
        View v = requireActivity().getLayoutInflater().inflate(R.layout.dialog_suggestion, null);
        ImageView img = v.findViewById(R.id.imgRestaurant);
        TextView tvName = v.findViewById(R.id.tvName);
        TextView tvDesc = v.findViewById(R.id.tvDesc);
        TextView tvAddress = v.findViewById(R.id.tvAddress);
        TextView tvPriceRange = v.findViewById(R.id.tvPriceRange);
        TextView tvOpeningHours = v.findViewById(R.id.tvOpeningHours);

        Glide.with(this).load(restaurant.getImage()).into(img);
        tvName.setText("🍽️ " + restaurant.getName());
        tvDesc.setText("📋 " + restaurant.getDescription());
        tvAddress.setText("📍 " + restaurant.getAddress());
        tvPriceRange.setText("💰 " + restaurant.getPriceRange());
        tvOpeningHours.setText("⏰ Giờ mở cửa: " + restaurant.getOpeningHours());
        AlertDialog dlg = new AlertDialog.Builder(requireContext())
                .setView(v)
                .create();

        v.findViewById(R.id.btnSuggestAgain).setOnClickListener(view -> {
            dlg.dismiss();
            if (listener != null) listener.onSuggestAgain();
        });
        v.findViewById(R.id.btnViewDetail).setOnClickListener(view -> {
            dlg.dismiss();
            if (listener != null) listener.onViewDetail(restaurant.getId());
        });

        return dlg;
    }
}

