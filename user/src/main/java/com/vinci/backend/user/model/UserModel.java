package com.vinci.backend.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.vinci.common.web.util.JsonUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 * Created by tim@vinci on 15-1-29.
 */
public class UserModel implements Serializable{
    @JsonIgnore
    private long id;
    private String userId;
    private String nickName;
    private String deviceIMEI;

    @JsonUnwrapped
    private UserSettings userSettings;

    private int version;
    private Date createDate;
    private Date updateTime;

    //下面的是数据库extra内容

    public static class UserSettings implements Serializable{
        /** 头像url，后面还要加上头像文件名，例如：s.jpg l.jpg**/
        private String headImgBaseUrl;

        /** 暂时先这些，后续有需求再加 **/
        public String getHeadImgBaseUrl() {
            return headImgBaseUrl;
        }

        public void setHeadImgBaseUrl(String headImgBaseUrl) {
            this.headImgBaseUrl = headImgBaseUrl;
        }

        @Override
        public String toString() {
            return JsonUtils.encode(this);
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getDeviceIMEI() {
        return deviceIMEI;
    }

    public void setDeviceIMEI(String deviceIMEI) {
        this.deviceIMEI = deviceIMEI;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", deviceIMEI=" + deviceIMEI +
                ", userSettings=" + userSettings +
                ", version=" + version +
                ", createDate=" + createDate +
                ", updateTime=" + updateTime +
                '}';
    }
}
