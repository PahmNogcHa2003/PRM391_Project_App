package com.example.prm391_project_apprestaurants.controllers.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm391_project_apprestaurants.R;
import com.example.prm391_project_apprestaurants.controllers.adapters.ReviewAdapter;
import com.example.prm391_project_apprestaurants.entities.Review;
import com.example.prm391_project_apprestaurants.services.ReviewService;

import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private EditText edtReviewContent;
    private RatingBar ratingBar;
    private Button btnSubmitReview;
    private RecyclerView rvReviews;

    private ReviewService reviewService;
    private ReviewAdapter reviewAdapter;
    private int restaurantId;
    private int currentUserId = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        edtReviewContent = findViewById(R.id.etReviewContent);
        ratingBar = findViewById(R.id.rbRating);
        btnSubmitReview = findViewById(R.id.btnSubmitReview);
        rvReviews = findViewById(R.id.rvReviews);

        restaurantId = getIntent().getIntExtra("restaurantId", -1);
        reviewService = new ReviewService(this);

        rvReviews.setLayoutManager(new LinearLayoutManager(this));
        loadReviews();

        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtReviewContent.getText().toString().trim();
                int rating = (int) ratingBar.getRating();

                if (content.isEmpty() || rating == 0) {
                    Toast.makeText(ReviewActivity.this, "Vui lòng nhập nội dung và chọn sao.", Toast.LENGTH_SHORT).show();
                    return;
                }

                reviewService.addReview(currentUserId, restaurantId, content, rating);
                Toast.makeText(ReviewActivity.this, "Đánh giá đã được gửi.", Toast.LENGTH_SHORT).show();
                edtReviewContent.setText("");
                ratingBar.setRating(0);
                loadReviews();
            }
        });
    }

    private void loadReviews() {
        List<Review> reviews = reviewService.getReviews(restaurantId);
        reviewAdapter = new ReviewAdapter(this, reviews, currentUserId, reviewService);
        rvReviews.setAdapter(reviewAdapter);
    }
}