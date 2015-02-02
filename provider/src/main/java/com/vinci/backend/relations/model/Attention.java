package com.vinci.backend.relations.model;

import java.io.Serializable;
import java.util.List;

/**
 * 一个关注
 * Created by tim@vinci on 15-1-30.
 */
public class Attention implements Serializable{

    public Attention() {
    }

    public Attention(boolean isAttention) {
        this.isAttention = isAttention;
    }

    /**
     * true： 关注
     * false： 粉丝
     */
    private boolean isAttention = true;

    /**
     * 总共的关注或粉丝数
     */
    private int totalCount;
    /**
     * 分页用id
     */
    private long lastId;

    private List<Long> userIds;

    public boolean isAttention() {
        return isAttention;
    }

    public void setAttention(boolean isAttention) {
        this.isAttention = isAttention;
    }


    public long getLastId() {
        return lastId;
    }

    public void setLastId(long lastId) {
        this.lastId = lastId;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public String toString() {
        return "Attention{" +
                "isAttention=" + isAttention +
                ", totalCount=" + totalCount +
                ", lastId=" + lastId +
                ", userIds=" + userIds +
                '}';
    }
}
