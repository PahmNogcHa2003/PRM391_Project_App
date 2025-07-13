package com.example.prm391_project_apprestaurants.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.detail.RestaurantDetailActivity;
import com.example.prm391_project_apprestaurants.controllers.fragments.SuggestionDialogFragment;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.services.SuggestionService;

public class SuggestionActivity extends AppCompatActivity {
    private SuggestionService service;
    private int selectedType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_choice);

        service = new SuggestionService(this);

        RadioGroup rg = findViewById(R.id.rgType);
        Button btn = findViewById(R.id.btnSuggest);

        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb1) {
                selectedType = 1;
            } else if (checkedId == R.id.rb2) {
                selectedType = 2;
            } else if (checkedId == R.id.rb3) {
                selectedType = 3;
            } else {
                selectedType = 0;
            }
        });
        btn.setOnClickListener(v -> {
            v.animate()
                    .scaleX(0.95f)
                    .scaleY(0.95f)
                    .setDuration(100)
                    .withEndAction(() -> {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(100).start();
                        triggerSuggest();
                    })
                    .start();
        });

    }



    private void triggerSuggest() {
        double lat = getLastKnownLat(), lng = getLastKnownLng();
        Restaurant res = service.getByType(selectedType, lat, lng);
        if (res == null) {
            Toast.makeText(this, "Không tìm thấy quán phù hợp.", Toast.LENGTH_SHORT).show();
            return;
        }
        SuggestionDialogFragment dlg = SuggestionDialogFragment.newInstance(res);
        dlg.setOnActionListener(new SuggestionDialogFragment.OnActionListener() {
            @Override
            public void onSuggestAgain() { triggerSuggest(); }
            @Override
            public void onViewDetail(int restId) {
                Intent i = new Intent(SuggestionActivity.this, RestaurantDetailActivity.class);
                i.putExtra("RESTAURANT_ID", restId);
                startActivity(i);
            }
        });
        dlg.show(getSupportFragmentManager(), "suggest");
    }


    private double getLastKnownLat() {
        return 21.02;
    }
    private double getLastKnownLng() {
        return 105.85;
    }
}

