package com.bestzpr.tally.util;

import com.bestzpr.tally.domain.entity.User;

/**
 * @className UserContext
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/11 23:39
 * @Version 1.0
 */
public class UserContext {
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static User getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(User user) {
        userThreadLocal.set(user);
    }

    public static void clear() {
        userThreadLocal.remove();
    }
}
