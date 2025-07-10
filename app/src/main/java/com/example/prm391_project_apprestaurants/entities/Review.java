package com.example.prm391_project_apprestaurants.entities;

public class Review {
    private int id;
    private int userId;
    private int restaurantId;
    private String content;
    private int rating;
    private String createdAt;
    private String userName;
    public Review() {}

    public Review(int id, int userId, int restaurantId, String content, int rating, String createdAt, String userName) {
        this.id = id;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.content = content;
        this.rating = rating;
        this.createdAt = createdAt;
        this.userName = userName;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getUserName() {return userName; }
    public void setUserName(String userName) {this.userName = userName; }
}