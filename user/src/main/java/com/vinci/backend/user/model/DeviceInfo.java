package com.vinci.backend.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * 数据库device表的
 * Created by tim@vinci on 15-1-28.
 */
public class DeviceInfo {
    @JsonIgnore
    private long id;
    private String imei;
    private String macAddr;
    private String userId;
    private Date createDate;
    private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
        return "DeviceInfo{" +
                "id=" + id +
                ", imei='" + imei + '\'' +
                ", macAddr='" + macAddr + '\'' +
                ", userId='" + userId + '\'' +
                ", createDate=" + createDate +
                ", updateTime=" + updateTime +
                '}';
    }
}
