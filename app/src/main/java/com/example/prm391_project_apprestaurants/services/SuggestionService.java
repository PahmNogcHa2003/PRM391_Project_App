package com.example.prm391_project_apprestaurants.services;

import android.content.Context;
import com.example.prm391_project_apprestaurants.dal.SuggestionDBContext;
import com.example.prm391_project_apprestaurants.entities.Restaurant;
import java.util.List;
import java.util.Random;

public class SuggestionService {
    private final SuggestionDBContext db;
    private final Random random = new Random();

    public SuggestionService(Context ctx) {
        db = new SuggestionDBContext(ctx);
    }

    public Restaurant getRandom() {
        return db.getRandomRestaurant();
    }

    public Restaurant getByMealTime() {
        return db.getSuggestedByMealTime();
    }

    public Restaurant getByRating() {
        return db.getHighestRatedRestaurant();
    }

    public Restaurant getByGPS(double lat, double lng) {
        return db.getNearestRestaurant(lat, lng);
    }

    public Restaurant getByType(int type, double lat, double lng) {
        switch (type) {
            case 0: return getRandom();
            case 1: return getByMealTime();
            case 2: return getByRating();
            case 3: return getByGPS(lat, lng);
            default: return null;
        }
    }
}
