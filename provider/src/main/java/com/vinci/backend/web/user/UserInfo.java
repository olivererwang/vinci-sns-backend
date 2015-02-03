package com.vinci.backend.web.user;

import com.vinci.backend.user.model.UserModel;

import java.io.Serializable;

/**
 * Created by tim@vinci on 15-1-27.
 */
public class UserInfo implements Serializable{

    //用户id
    private long userId;
    //用户昵称
    private String userName;
    //用户访问ip
    private String userIp;
    //用户当前注册的手机号
    private String userMobile;
    //当前绑定的设备id
    private String deviceId;
    /**App版本号*/
    private String vid;
    //从哪个网站oauth过来的
    private String oauthUserName;
    //authToken
    private String authToken;

    private UserModel user;

    /**是否是系统用户，如果是则跳过用户权限校验*/
    private boolean system;

    public boolean isSystem() {
        return system;
    }
    public void setSystem(boolean system) {
        this.system = system;
    }
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getOauthUserName() {
        return oauthUserName;
    }

    public void setOauthUserName(String oauthUserName) {
        this.oauthUserName = oauthUserName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public static UserInfo systemUserInfo(){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(-1);
        userInfo.setUserName("system_user");
        userInfo.setSystem(true);
        return userInfo;
    }
}
