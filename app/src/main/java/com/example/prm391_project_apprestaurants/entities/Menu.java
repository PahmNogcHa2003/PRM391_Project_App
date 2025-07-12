package com.example.prm391_project_apprestaurants.entities;

public class Menu {
    private int id;
    private int restaurantId;
    private String dishName;
    private String price;
    private String imageUrl;
    private String description;
    private String createdAt;

    // Constructor rỗng
    public Menu() {
    }

    // Constructor đầy đủ
    public Menu(int id, int restaurantId, String dishName, String price, String imageUrl, String description, String createdAt) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.dishName = dishName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Optional: toString() để debug
    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", restaurantId=" + restaurantId +
                ", dishName='" + dishName + '\'' +
                ", price='" + price + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}