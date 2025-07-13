package com.example.prm391_project_apprestaurants.services;

import android.content.Context;

import com.example.prm391_project_apprestaurants.dal.RestaurantDBContext;
import com.example.prm391_project_apprestaurants.dal.UserDBContext;
import com.example.prm391_project_apprestaurants.entities.User;
import com.example.prm391_project_apprestaurants.requests.SearchUserRequest;

import java.util.List;

public class UserService {
    private final UserDBContext userDBContext;

    public UserService(Context context) {
        userDBContext = new UserDBContext(context);
    }

    public List<User> getAllUsers(SearchUserRequest request) {
        return userDBContext.getAllUsers(request);
    }

    public User getUserById(int id) {
        return userDBContext.getUserById(id);
    }

    public boolean updateUser(User user) {
        return userDBContext.updateUser(user);
    }

    public long countTotalUsers(SearchUserRequest request) {
        return userDBContext.countTotalUsers(request);
    }
}
