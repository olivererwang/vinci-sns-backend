package com.vinci.common.base.user;

/**
 * 用户上下文，使用ThreadLocal存储从UserInfoFilter中获取到的UserInfo对象
 * Created by tim@vinci on 15-1-27.
 */
public class UserContext {
    private static final ThreadLocal<UserInfo> userInfoLocal = new ThreadLocal<UserInfo>();

    public static void setUserInfo(UserInfo userInfo) {
        userInfoLocal.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return userInfoLocal.get();
    }
}