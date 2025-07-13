//package com.example.prm391_project_apprestaurants.services;
//
//import android.content.Context;
//
//import com.example.prm391_project_apprestaurants.dal.SuggestionDbContext;
//import com.example.prm391_project_apprestaurants.entities.Restaurant;
//
//import java.util.List;
//
//public class SuggestionService {
//    private final SuggestionDbContext suggestionDbContext;
//
//    public SuggestionService(Context context) {
//        suggestionDbContext = new SuggestionDbContext(context);
//    }
//
//    // 1. Gợi ý ngẫu nhiên
//    public Restaurant getRandomSuggestion() {
//        return suggestionDbContext.getRandomRestaurant();
//    }
//
//    // 2. Gợi ý theo vị trí
//    public Restaurant getClosestSuggestion(double lat, double lng) {
//        return suggestionDbContext.getClosestRestaurant(lat, lng);
//    }
//
//    // 3. Gợi ý theo đánh giá cao nhất
//    public Restaurant getTopRatedSuggestion() {
//        return suggestionDbContext.getTopRatedRestaurant();
//    }
//
//    // 4. Gợi ý theo bữa ăn
//    public List<Restaurant> getSuggestionsByMealTime(String mealTime) {
//        return suggestionDbContext.getRestaurantsByMealTime(mealTime);
//    }
//
//    // Tất cả quán được hiển thị
//    public List<Restaurant> getAllVisible() {
//        return suggestionDbContext.getAllVisibleRestaurants();
//    }
//}
