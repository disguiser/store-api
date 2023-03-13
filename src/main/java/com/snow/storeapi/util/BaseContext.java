package com.snow.storeapi.util;

import com.snow.storeapi.entity.User;

public class BaseContext {
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void setCurrentUser(User user) {
        threadLocal.set(user);
    }

    public static User getCurrentUser() {
        return threadLocal.get();
    }
}
