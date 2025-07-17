package com.example.prm391_project_apprestaurants.services;

import android.content.Context;
import com.example.prm391_project_apprestaurants.dal.ReviewDBContext;
import com.example.prm391_project_apprestaurants.dal.ReviewMediaDBContext;
import com.example.prm391_project_apprestaurants.entities.ReviewStatistic;
import com.example.prm391_project_apprestaurants.entities.Review;

import java.util.List;

public class ReviewService {
    private final ReviewDBContext reviewDBContext;
    private final ReviewMediaDBContext reviewMediaDBContext;

    public ReviewService(Context context) {
        this.reviewDBContext = new ReviewDBContext(context);
        this.reviewMediaDBContext = new ReviewMediaDBContext(context);
    }

    public List<Review> getReviews(int restaurantId) {
        List<Review> reviews = reviewDBContext.getReviewsByRestaurantId(restaurantId);
        for (Review r : reviews) {
            List<String> mediaUrls = reviewMediaDBContext.getMediaUrlsByReviewId(r.getId());
            r.setMediaUrls(mediaUrls);
        }
        return reviews;
    }


    public void addReview(int userId, int restaurantId, String content, int rating) {
        reviewDBContext.addReview(userId, restaurantId, content, rating);
    }

    public void updateReview(int reviewId, String newContent, int newRating) {
        reviewDBContext.updateReview(reviewId, newContent, newRating);
    }
    public List<String> getMediaUrls(int reviewId) {
        return reviewMediaDBContext.getMediaUrlsByReviewId(reviewId);
    }

    public List<Review> getReviewsByUserId(int userId) {
        return reviewDBContext.getReviewsByUserId(userId);
    }

    public void deleteAllMedia(int reviewId) {
        reviewMediaDBContext.deleteAllMediaForReview(reviewId);
    }

    public void addMedia(int reviewId, String imagePath) {
        reviewMediaDBContext.addMedia(reviewId, imagePath);
    }

    public void deleteReview(int reviewId) {
        reviewDBContext.deleteReview(reviewId);
    }

    public long countTotalReviews() {
        return reviewDBContext.countTotalReviews();
    }

    public List<ReviewStatistic> getReviewStatistics() {
        return reviewDBContext.getReviewStatistics();
    }

    public float getAverageRating() {
        return reviewDBContext.getAverageRating();
    }
}
