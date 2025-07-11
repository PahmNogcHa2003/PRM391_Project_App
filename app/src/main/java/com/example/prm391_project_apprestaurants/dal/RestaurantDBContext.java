package com.example.prm391_project_apprestaurants.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prm391_project_apprestaurants.entities.Restaurant;
import com.example.prm391_project_apprestaurants.requests.SearchRestaurantRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantDBContext {
    private final DbContext dbContext;

    public RestaurantDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    public List<Restaurant> getRestaurants(SearchRestaurantRequest request) {
        List<Restaurant> restaurants = new ArrayList<>();
        long totalElement = countTotalRestaurants(request);
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Restaurants r";
            List<String> params = new ArrayList<>();
            query += buildSearchRestaurantQuery(request, true, params, totalElement).toString();
            Cursor cursor = db.rawQuery(query, params.toArray(new String[0]));
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String address = cursor.getString(3);
                String description = cursor.getString(2);
                String image = cursor.getString(10);
                String priceRange = cursor.getString(5);
                String website = cursor.getString(9);
                String category = cursor.getString(6);
                String district = cursor.getString(4);
                Boolean isHidden = cursor.getInt(13) == 1;
                Restaurant restaurant = new Restaurant();
                restaurant.setId(id);
                restaurant.setName(name);
                restaurant.setAddress(address);
                restaurant.setDescription(description);
                restaurant.setImage(image);
                restaurant.setPriceRange(priceRange);
                restaurant.setWebsite(website);
                restaurant.setCategory(category);
                restaurant.setDistrict(district);
                restaurant.setHidden(isHidden);
                restaurant.setReviewCount(countReviewByRestaurantId(id));
                restaurants.add(restaurant);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return restaurants;
    }

    StringBuilder buildSearchRestaurantQuery(SearchRestaurantRequest request, boolean isPagination, List<String> params, long totalElement) {
        StringBuilder query = new StringBuilder();
        query.append(" WHERE 1=1 ");

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            query.append(" AND (r.Name LIKE ? OR r.Description LIKE ? OR r.Address LIKE ?) ");
            String keyword = "%" + request.getKeyword() + "%";
            params.add(keyword);
            params.add(keyword);
            params.add(keyword);
        }
        if (isPagination) {
            int totalPage = (int) Math.ceil(totalElement * 1.0 / request.getPageSize());
            query.append(" LIMIT ? OFFSET ? ");
            int correctPage = request.getPage() <= totalPage ? request.getPage() : 1;
            request.setPage(correctPage);
            request.setTotalPage(totalPage);
            params.add(String.valueOf(request.getPageSize()));
            params.add(String.valueOf((correctPage - 1) * request.getPageSize()));
        }

        return query;
    }

    public Restaurant findById(int id) {
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Restaurants WHERE id = ?";
            Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
            if (cursor.moveToNext()) {
                String name = cursor.getString(1);
                String address = cursor.getString(3);
                String description = cursor.getString(2);
                String image = cursor.getString(10);
                String priceRange = cursor.getString(5);
                String website = cursor.getString(9);
                String category = cursor.getString(6);
                String district = cursor.getString(4);
                String openning = cursor.getString(7);
                String phoneNumber = cursor.getString(8);
                double latitude = cursor.getDouble(11);
                double longitude = cursor.getDouble(12);
                Restaurant restaurant = new Restaurant();
                restaurant.setId(id);
                restaurant.setName(name);
                restaurant.setAddress(address);
                restaurant.setDescription(description);
                restaurant.setImage(image);
                restaurant.setPriceRange(priceRange);
                restaurant.setWebsite(website);
                restaurant.setCategory(category);
                restaurant.setDistrict(district);
                restaurant.setOpeningHours(openning);
                restaurant.setPhoneNumber(phoneNumber);
                restaurant.setReviewCount(countReviewByRestaurantId(id));
                restaurant.setLatitude(latitude);
                restaurant.setLongitude(longitude);
                return restaurant;
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return null;
    }

    public long countTotalRestaurants(SearchRestaurantRequest request) {
        long count = 0;
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT COUNT(r.id) FROM Restaurants r";
            List<String> params = new ArrayList<>();
            query += buildSearchRestaurantQuery(request, false, params, 0).toString();
            Cursor cursor = db.rawQuery(query, params.toArray(new String[0]));
            if (cursor.moveToFirst()) {
                count = cursor.getLong(0);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return count;
    }

    public int countReviewByRestaurantId(int id) {
        int count = 0;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbContext.getReadableDatabase();
            String query = "SELECT COUNT(r.RestaurantId) FROM Reviews r WHERE r.RestaurantId = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        } finally {
            if (cursor != null) cursor.close();
            if (db != null) db.close();
        }
        return count;
    }

    public List<Restaurant> getAllRestaurantsWithFilter(String priceRange, String district, String category, String searchQuery) {
        List<Restaurant> restaurants = new ArrayList<>();
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Restaurants WHERE 1=1";
            List<String> selectionArgs = new ArrayList<>();

            if (priceRange != null && !priceRange.isEmpty()) {
                query += " AND PriceRange = ?";
                selectionArgs.add(priceRange);
            }
            if (district != null && !district.isEmpty()) {
                query += " AND District = ?";
                selectionArgs.add(district);
            }
            if (category != null && !category.isEmpty()) {
                query += " AND Category = ?";
                selectionArgs.add(category);
            }
            if (searchQuery != null && !searchQuery.isEmpty()) {
                query += " AND Name LIKE ?";
                selectionArgs.add("%" + searchQuery + "%");
            }

            Cursor cursor = db.rawQuery(query, selectionArgs.toArray(new String[0]));
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String address = cursor.getString(3);
                String description = cursor.getString(2);
                String image = cursor.getString(10);
                String priceRangeValue = cursor.getString(5);
                String website = cursor.getString(9);
                String categoryValue = cursor.getString(6);
                String districtValue = cursor.getString(4);
                Restaurant restaurant = new Restaurant();
                restaurant.setId(id);
                restaurant.setName(name);
                restaurant.setAddress(address);
                restaurant.setDescription(description);
                restaurant.setImage(image);
                restaurant.setPriceRange(priceRangeValue);
                restaurant.setWebsite(website);
                restaurant.setCategory(categoryValue);
                restaurant.setDistrict(districtValue);
                restaurants.add(restaurant);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public boolean updateRestaurant(Restaurant restaurant) {
        boolean result = false;
        try {
            SQLiteDatabase db = dbContext.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Name", restaurant.getName());
            values.put("Address", restaurant.getAddress());
            values.put("Description", restaurant.getDescription());
            values.put("ImageUrl", restaurant.getImage());
            values.put("PriceRange", restaurant.getPriceRange());
            values.put("Website", restaurant.getWebsite());
            values.put("Category", restaurant.getCategory());
            values.put("District", restaurant.getDistrict());
            values.put("OpeningHours", restaurant.getOpeningHours());
            values.put("PhoneNumber", restaurant.getPhoneNumber());
            values.put("Latitude", restaurant.getLatitude());
            values.put("Longitude", restaurant.getLongitude());
            values.put("updatedAt", LocalDateTime.now().toString());
            result = db.update("Restaurants", values, "Id = ?", new String[]{String.valueOf(restaurant.getId())}) > 0;
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return result;
    }

    public boolean deleteRestaurant(Restaurant restaurant) {
        boolean result = false;
        try {
            SQLiteDatabase db = dbContext.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("IsHidden", 1);
            values.put("updatedAt", LocalDateTime.now().toString());
            result = db.update("Restaurants", values, "Id = ?", new String[]{String.valueOf(restaurant.getId())}) > 0;
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return result;
    }

    public boolean activeRestaurant(Restaurant restaurant) {
        boolean result = false;
        try {
            SQLiteDatabase db = dbContext.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("IsHidden", 0);
            values.put("updatedAt", LocalDateTime.now().toString());
            result = db.update("Restaurants", values, "Id = ?", new String[]{String.valueOf(restaurant.getId())}) > 0;
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return result;
    }
}
