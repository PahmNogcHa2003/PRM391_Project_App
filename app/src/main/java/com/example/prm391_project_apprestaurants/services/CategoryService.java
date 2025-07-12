package com.example.prm391_project_apprestaurants.services;

import android.content.Context;

import com.example.prm391_project_apprestaurants.dal.CategoryDBContext;
import com.example.prm391_project_apprestaurants.entities.Category;

import java.util.List;

public class CategoryService {
    private final CategoryDBContext categoryDBContext;
    public CategoryService(Context context) {
        categoryDBContext = new CategoryDBContext(context);
    }

    public List<Category> getAllCategories(){
        return categoryDBContext.getAllCategories();
    }
}
