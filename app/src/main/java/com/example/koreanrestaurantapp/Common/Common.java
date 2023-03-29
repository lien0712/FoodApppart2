package com.example.koreanrestaurantapp.Common;

import com.example.koreanrestaurantapp.model.User;

public class Common {
    public static User currentUser;

    public Common() {
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Common.currentUser = currentUser;
    }
}
