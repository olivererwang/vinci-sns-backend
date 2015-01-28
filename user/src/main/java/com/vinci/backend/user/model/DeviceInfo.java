package com.vinci.backend.user.model;

/**
 * 数据库device表的
 * Created by tim@vinci on 15-1-28.
 */
public class DeviceInfo {
    private long id;
    private String imei;
    private String macAddr;
    private String userId;

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
}
