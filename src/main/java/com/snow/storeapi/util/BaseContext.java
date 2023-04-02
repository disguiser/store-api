package com.snow.storeapi.util;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.snow.storeapi.entity.User;

public class BaseContext {
    private static final TransmittableThreadLocal<User> threadLocal = new TransmittableThreadLocal<>();

    public static void setCurrentUser(User user) {
        threadLocal.set(user);
    }

    public static User getCurrentUser() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
