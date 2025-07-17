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
                String openingHours = cursor.getString(7);
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
                restaurant.setOpeningHours(openingHours);
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

    public List<Restaurant> getAllRestaurants() {
        List<Restaurant> restaurants = new ArrayList<>();
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT * FROM Restaurants r";
            Cursor cursor = db.rawQuery(query, null);
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

    public boolean createRestaurant(Restaurant restaurant, boolean isClose) {
        try {
            SQLiteDatabase db = dbContext.getDb() != null ? dbContext.getDb() : dbContext.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Name", restaurant.getName());
            values.put("Address", restaurant.getAddress());
            values.put("Description", restaurant.getDescription());
            values.put("PriceRange", restaurant.getPriceRange());
            values.put("Website", restaurant.getWebsite());
            values.put("Category", restaurant.getCategory());
            values.put("District", restaurant.getDistrict());
            values.put("OpeningHours", restaurant.getOpeningHours());
            values.put("PhoneNumber", restaurant.getPhoneNumber());
            values.put("Latitude", restaurant.getLatitude());
            values.put("Longitude", restaurant.getLongitude());
            values.put("ImageUrl", restaurant.getImage());
            long result = db.insert("Restaurants", null, values);
            if (isClose) {
                db.close();
            }
            if (result > 0) {
                restaurant.setId((int) result);
            }
            return result > 0;
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return false;
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
        SQLiteDatabase db = dbContext.getReadableDatabase();
        String selection = "IsHidden = 0"; // Default filter
        List<String> selectionArgs = new ArrayList<>();

        if (priceRange != null && !priceRange.isEmpty()) {
            String[] priceRanges = priceRange.split("\\|");
            selection += " AND PriceRange IN (" + makePlaceholders(priceRanges.length) + ")";
            for (String range : priceRanges) {
                selectionArgs.add(range);
            }
        }
        if (district != null && !district.isEmpty() && !district.equals("Huyện")) {
            selection += " AND District = ?";
            selectionArgs.add(district);
        }
        if (category != null && !category.isEmpty() && !category.equals("Loại món")) {
            selection += " AND Id IN (SELECT RestaurantId FROM RestaurantCategory WHERE CategoryId = (SELECT Id FROM Categories WHERE Name = ?))";
            selectionArgs.add(category);
        }
        if (searchQuery != null && !searchQuery.isEmpty()) {
            selection += " AND Name LIKE ?";
            selectionArgs.add("%" + searchQuery + "%");
        }

        Log.d("FilterQuery", "Executing query with selection: " + selection + ", args: " + String.join(", ", selectionArgs));
        Cursor cursor = db.rawQuery("SELECT * FROM Restaurants WHERE " + selection, selectionArgs.toArray(new String[0]));
        if (cursor.moveToFirst()) {
            do {
                Restaurant restaurant = new Restaurant();
                restaurant.setId(cursor.getInt(cursor.getColumnIndexOrThrow("Id")));
                restaurant.setName(cursor.getString(cursor.getColumnIndexOrThrow("Name")));
                restaurant.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("Address")));
                restaurant.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("Description")));
                restaurant.setImage(cursor.getString(cursor.getColumnIndexOrThrow("ImageUrl")));
                restaurant.setPriceRange(cursor.getString(cursor.getColumnIndexOrThrow("PriceRange")));
                restaurant.setWebsite(cursor.getString(cursor.getColumnIndexOrThrow("Website")));
                restaurant.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("Category")));
                restaurant.setDistrict(cursor.getString(cursor.getColumnIndexOrThrow("District")));
                restaurant.setOpeningHours(cursor.getString(cursor.getColumnIndexOrThrow("OpeningHours")));
                restaurant.setHidden(cursor.getInt(cursor.getColumnIndexOrThrow("IsHidden")) == 1);
                restaurant.setReviewCount(countReviewByRestaurantId(restaurant.getId()));
                restaurants.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d("FilterResult", "Fetched " + restaurants.size() + " restaurants");
        return restaurants;
    }

    public boolean updateRestaurant(Restaurant restaurant, boolean isClose) {
        boolean result = false;
        try {
            SQLiteDatabase db = dbContext.getDb() != null ? dbContext.getDb() : dbContext.getWritableDatabase();
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
            if (isClose) {
                db.close();
            }
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

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Name FROM Categories", null);
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }

    private String makePlaceholders(int len) {
        String[] placeholders = new String[len];
        for (int i = 0; i < len; i++) {
            placeholders[i] = "?";
        }
        return String.join(",", placeholders);
    }
}