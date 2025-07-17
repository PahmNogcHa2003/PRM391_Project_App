package com.example.prm391_project_apprestaurants.dal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.prm391_project_apprestaurants.entities.HomeRestaurant;
import com.example.prm391_project_apprestaurants.entities.ReviewStatistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantDetailDBContext {
    private final DbContext dbContext;

    public RestaurantDetailDBContext(Context context) {
        dbContext = DbContext.getInstance(context);
    }

    // LẤY CATEGORIES ĐÚNG CHUẨN: JOIN BẢNG
    public List<String> getCategoriesByRestaurantId(int restaurantId) {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT c.Name FROM RestaurantCategory rc " +
                        "JOIN Categories c ON rc.CategoryId = c.Id " +
                        "WHERE rc.RestaurantId = ?",
                new String[]{String.valueOf(restaurantId)}
        );
        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return categories;
    }

    public List<HomeRestaurant> getAllRestaurants() {
        List<HomeRestaurant> list = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Restaurants WHERE IsHidden = 0", null);
        if (cursor.moveToFirst()) {
            do {
                HomeRestaurant restaurant = cursorToRestaurant(cursor);

                restaurant.setRating(getAverageRatingForRestaurant(restaurant.getId()));
                restaurant.setFavoriteCount(getFavoriteCountForRestaurant(restaurant.getId()));
                restaurant.setReviewCount(getReviewCountForRestaurant(restaurant.getId()));

                // LẤY CATEGORY CHUẨN
                List<String> categories = getCategoriesByRestaurantId(restaurant.getId());
                restaurant.setCategory(categories.isEmpty() ? "" : joinCategoryList(categories));

                list.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<HomeRestaurant> getTop10Restaurants() {
        List<HomeRestaurant> list = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM Restaurants WHERE IsHidden = 0 ORDER BY CreatedAt DESC LIMIT 10", null);
        if (cursor.moveToFirst()) {
            do {
                HomeRestaurant restaurant = cursorToRestaurant(cursor);

                restaurant.setRating(getAverageRatingForRestaurant(restaurant.getId()));
                restaurant.setFavoriteCount(getFavoriteCountForRestaurant(restaurant.getId()));
                restaurant.setReviewCount(getReviewCountForRestaurant(restaurant.getId()));

                // LẤY CATEGORY CHUẨN
                List<String> categories = getCategoriesByRestaurantId(restaurant.getId());
                restaurant.setCategory(categories.isEmpty() ? "" : joinCategoryList(categories));

                list.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<HomeRestaurant> getTop10FavoriteRestaurantsByRating() {
        List<HomeRestaurant> list = new ArrayList<>();
        SQLiteDatabase db = dbContext.getReadableDatabase();
        String sql = "SELECT r.*, " +
                "COUNT(f.Id) AS favoriteCount, " +
                "AVG(rv.Rating) AS avgRating, " +
                "COUNT(rv.Id) AS reviewCount " +
                "FROM Restaurants r " +
                "LEFT JOIN Favorites f ON r.Id = f.RestaurantId " +
                "LEFT JOIN Reviews rv ON r.Id = rv.RestaurantId " +
                "WHERE r.IsHidden = 0 " +
                "GROUP BY r.Id " +
                "ORDER BY favoriteCount DESC, avgRating DESC " +
                "LIMIT 10";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                HomeRestaurant restaurant = cursorToRestaurant(cursor);

                int favoriteCount = cursor.getInt(cursor.getColumnIndexOrThrow("favoriteCount"));
                double avgRating = cursor.getDouble(cursor.getColumnIndexOrThrow("avgRating"));
                int reviewCount = cursor.getInt(cursor.getColumnIndexOrThrow("reviewCount"));
                restaurant.setFavoriteCount(favoriteCount);
                restaurant.setRating(avgRating);
                restaurant.setReviewCount(reviewCount);

                // LẤY CATEGORY CHUẨN
                List<String> categories = getCategoriesByRestaurantId(restaurant.getId());
                restaurant.setCategory(categories.isEmpty() ? "" : joinCategoryList(categories));

                list.add(restaurant);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public HomeRestaurant getRestaurantById(int id) {
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Restaurants WHERE Id = ?", new String[]{String.valueOf(id)});
        HomeRestaurant restaurant = null;
        if (cursor.moveToFirst()) {
            restaurant = cursorToRestaurant(cursor);

            restaurant.setRating(getAverageRatingForRestaurant(restaurant.getId()));
            restaurant.setFavoriteCount(getFavoriteCountForRestaurant(restaurant.getId()));
            restaurant.setReviewCount(getReviewCountForRestaurant(restaurant.getId()));

            // LẤY CATEGORY CHUẨN
            List<String> categories = getCategoriesByRestaurantId(restaurant.getId());
            restaurant.setCategory(categories.isEmpty() ? "" : joinCategoryList(categories));
        }
        cursor.close();
        db.close();
        return restaurant;
    }

    public double getAverageRatingForRestaurant(int restaurantId) {
        double rating = 0.0;
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT AVG(Rating) FROM Reviews WHERE RestaurantId=?",
                new String[]{String.valueOf(restaurantId)}
        );
        if (cursor.moveToFirst()) {
            rating = cursor.isNull(0) ? 0.0 : cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return rating;
    }

    public int getFavoriteCountForRestaurant(int restaurantId) {
        int count = 0;
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Favorites WHERE RestaurantId=?",
                new String[]{String.valueOf(restaurantId)}
        );
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    public int getReviewCountForRestaurant(int restaurantId) {
        int count = 0;
        SQLiteDatabase db = dbContext.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Reviews WHERE RestaurantId=?",
                new String[]{String.valueOf(restaurantId)}
        );
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    /**
     * Chuyển dữ liệu từ Cursor thành đối tượng HomeRestaurant,
     * category sẽ được set lại ngoài hàm này (sau khi lấy từ bảng liên kết thực sự)
     */
    private HomeRestaurant cursorToRestaurant(Cursor cursor) {
        return new HomeRestaurant(
                cursor.getInt(cursor.getColumnIndexOrThrow("Id")),
                cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                cursor.getString(cursor.getColumnIndexOrThrow("Address")),
                cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")),
                "", // email
                cursor.getString(cursor.getColumnIndexOrThrow("Website")),
                cursor.getString(cursor.getColumnIndexOrThrow("Description")),
                cursor.getString(cursor.getColumnIndexOrThrow("OpeningHours")),
                "", // category SẼ ĐƯỢC SET LẠI (không lấy từ trường cũ)
                cursor.getString(cursor.getColumnIndexOrThrow("PriceRange")),
                cursor.getString(cursor.getColumnIndexOrThrow("District")),
                0.0,
                0,
                0,
                cursor.getString(cursor.getColumnIndexOrThrow("ImageUrl")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("Latitude")),
                cursor.getDouble(cursor.getColumnIndexOrThrow("Longitude")),
                false,
                false
        );
    }

    // JOIN danh sách category bằng dấu phẩy hoặc lựa chọn ký tự khác nếu muốn
    private String joinCategoryList(List<String> categoryList) {
        if (categoryList == null || categoryList.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < categoryList.size(); i++) {
            sb.append(categoryList.get(i));
            if (i != categoryList.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public List<ReviewStatistic> getReviewStatisticsByRestaurantId(int restaurantId) {
        List<ReviewStatistic> statistics = new ArrayList<>();
        try {
            SQLiteDatabase db = dbContext.getReadableDatabase();
            String query = "SELECT " +
                    "r.Rating, " +
                    "COUNT(r.Rating) AS TotalRating, " +
                    "ROUND(COUNT(r.Rating) * 100.0 / (SELECT COUNT(*) FROM Reviews WHERE RestaurantId = ?), 2) AS Percent " +
                    "FROM Reviews r " +
                    "WHERE r.RestaurantId = ? " +
                    "GROUP BY r.Rating " +
                    "ORDER BY r.Rating;";
            Cursor cursor = db.rawQuery(query, new String[]{
                    String.valueOf(restaurantId),
                    String.valueOf(restaurantId)
            });

            while (cursor.moveToNext()) {
                int rating = cursor.getInt(0);
                int count = cursor.getInt(1);
                float percent = cursor.getFloat(2);
                ReviewStatistic statistic = new ReviewStatistic();
                statistic.setRating(rating);
                statistic.setQuantity(count);
                statistic.setPercentage(percent);
                statistics.add(statistic);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.d("Error", Objects.requireNonNull(e.getMessage()));
        }
        return statistics;
    }
}
