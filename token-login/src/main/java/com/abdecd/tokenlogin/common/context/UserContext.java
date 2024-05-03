package com.abdecd.tokenlogin.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

public class UserContext {
    private static final ThreadLocal<UserContextHolder> userContext = TransmittableThreadLocal.withInitial(UserContextHolder::new);

    public static void setUserId(Long userId) {
        userContext.get().setUserId(userId);
    }

    public static Long getUserId() {
        return userContext.get().getUserId();
    }

    public static void setPermission(String permission) {
        userContext.get().setPermission(permission);
    }

    public static String getPermission() {
        return userContext.get().getPermission();
    }

    public static void clear() {
        userContext.remove();
    }

    public static class UserContextHolder {
        private Long userId;
        private String permission;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getPermission() {
            return permission;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }
    }
}
