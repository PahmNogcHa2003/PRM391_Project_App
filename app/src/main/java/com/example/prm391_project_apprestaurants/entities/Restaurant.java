package com.example.prm391_project_apprestaurants.entities;

import android.database.Cursor;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prm391_project_apprestaurants.dal.DbContext;
public class Restaurant {
    private int id;
    private String name;
    private String description;
    private String address;
    private String district;
    private String priceRange;
    private String category;
    private String openingHours;
    private String phoneNumber;
    private String website;
    private String imageUrl;
    private double latitude;
    private double longitude;
    private int isHidden;
    private String mealTime;

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getOpeningHours() { return openingHours; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public int getIsHidden() { return isHidden; }
    public void setIsHidden(int isHidden) { this.isHidden = isHidden; }

    public String getMealTime() { return mealTime; }
    public void setMealTime(String mealTime) { this.mealTime = mealTime; }

    // --- fromCursor(): Dùng để map từ Cursor sang object ---
    public static Restaurant fromCursor(Cursor cursor) {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
        restaurant.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
        restaurant.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("Description")));
        restaurant.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("Address")));
        restaurant.setDistrict(cursor.getString(cursor.getColumnIndexOrThrow("District")));
        restaurant.setPriceRange(cursor.getString(cursor.getColumnIndexOrThrow("PriceRange")));
        restaurant.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("Category")));
        restaurant.setOpeningHours(cursor.getString(cursor.getColumnIndexOrThrow("OpeningHours")));
        restaurant.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")));
        restaurant.setWebsite(cursor.getString(cursor.getColumnIndexOrThrow("Website")));
        restaurant.setImageUrl(cursor.getString(cursor.getColumnIndexOrThrow("ImageUrl")));
        restaurant.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Latitude")));
        restaurant.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("Longitude")));
        restaurant.setIsHidden(cursor.getInt(cursor.getColumnIndexOrThrow("IsHidden")));
        restaurant.setMealTime(cursor.getString(cursor.getColumnIndexOrThrow("MealTime")));
        return restaurant;
    }
    public double getRating(Context context) {
        DbContext dbContext = DbContext.getInstance(context);
        SQLiteDatabase db = dbContext.getReadableDatabase();
        double rating = 0;
        Cursor cursor = db.rawQuery(
                "SELECT AVG(Rating) FROM Reviews WHERE RestaurantId = ?",
                new String[]{String.valueOf(this.id)}
        );

        if (cursor.moveToFirst()) {
            rating = cursor.isNull(0) ? 0 : cursor.getDouble(0);
        }
        cursor.close();
        return rating;
    }
}
