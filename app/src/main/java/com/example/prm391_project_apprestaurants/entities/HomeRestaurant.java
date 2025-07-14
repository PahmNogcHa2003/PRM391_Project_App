package com.example.prm391_project_apprestaurants.entities;

/**
 * Mô hình dữ liệu cho Nhà hàng (Restaurant)
 * Đã bổ sung trường favoriteCount để hỗ trợ top 10 yêu thích,
 * đồng bộ với các truy vấn lấy top theo số lượt yêu thích và rating trung bình.
 */
public class HomeRestaurant {
    private int id;                     // ID nhà hàng
    private String name;                // Tên nhà hàng
    private String address;             // Địa chỉ
    private String phone;               // Số điện thoại
    private String email;               // Email liên hệ
    private String website;             // Website
    private String description;         // Mô tả ngắn
    private String openingHours;        // Giờ mở cửa (ví dụ: "08:00 - 22:00")
    private String category;            // Loại hình (ví dụ: "Nhà hàng", "Cafe", ...)
    private String price;               // Mức giá
    private String district;            // Quận/Huyện
    private double rating;              // Điểm đánh giá trung bình
    private int reviewCount;            // Số lượng đánh giá
    private int favoriteCount;          // Số lượt yêu thích (bổ sung)
    private String imageUrl;            // Ảnh đại diện nhà hàng
    private double latitude;            // Vĩ độ (tọa độ bản đồ)
    private double longitude;           // Kinh độ (tọa độ bản đồ)
    private boolean isFavorite;         // Đã yêu thích chưa
    private boolean isSaved;            // Đã lưu địa điểm chưa

    // Constructor mặc định
    public HomeRestaurant() {
    }

    // Constructor đầy đủ trường
    public HomeRestaurant(int id, String name, String address, String phone, String email, String website,
                          String description, String openingHours, String category, String price, String district,
                          double rating, int reviewCount, int favoriteCount, String imageUrl, double latitude, double longitude,
                          boolean isFavorite, boolean isSaved) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.description = description;
        this.openingHours = openingHours;
        this.category = category;
        this.price = price;
        this.district = district;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.favoriteCount = favoriteCount;
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isFavorite = isFavorite;
        this.isSaved = isSaved;
    }

    // Getter & Setter cho tất cả các trường

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }
}
