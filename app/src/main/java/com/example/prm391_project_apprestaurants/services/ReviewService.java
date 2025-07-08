package com.example.prm391_project_apprestaurants.services;
import android.content.Context;

import com.example.prm391_project_apprestaurants.dal.ReviewDBContext;
import com.example.prm391_project_apprestaurants.entities.Review;

import java.util.List;
public class ReviewService {
    private final ReviewDBContext reviewDBContext;

    public ReviewService(Context context) {
        this.reviewDBContext = new ReviewDBContext(context);
    }

    public List<Review> getReviews(int restaurantId) {
        return reviewDBContext.getReviewsByRestaurantId(restaurantId);
    }

    public void addReview(int userId, int restaurantId, String content, int rating) {
        reviewDBContext.addReview(userId, restaurantId, content, rating);
    }

    public void updateReview(int reviewId, String newContent) {
        reviewDBContext.updateReview(reviewId, newContent);
    }

    public void deleteReview(int reviewId) {
        reviewDBContext.deleteReview(reviewId);
    }
}
