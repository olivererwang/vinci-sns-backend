package com.vinci.backend.domain.feed.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tim@vinci on 15-2-2.
 */
public class TimelineModel implements Serializable{

    /** feed id**/
    private long id;

    /** userid **/
    private long userid;

    /** 实际内容 **/
    private long feed;

    /** 引用的feed id**/
    private long refFeed;

    /** 发表时间 **/
    private Date createDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public long getFeed() {
        return feed;
    }

    public void setFeed(long feed) {
        this.feed = feed;
    }

    public long getRefFeed() {
        return refFeed;
    }

    public void setRefFeed(long refFeed) {
        this.refFeed = refFeed;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "TimelineModel{" +
                "id=" + id +
                ", userid=" + userid +
                ", feed=" + feed +
                ", refFeed=" + refFeed +
                ", createDate=" + createDate +
                '}';
    }
}
