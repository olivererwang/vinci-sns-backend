package com.vinci.backend.domain.feed.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应
 * Created by tim@vinci on 15-2-2.
 */
public class FeedModel implements Serializable{

    /** feed id**/
    private long id;

    /** userid **/
    private long userid;

    /** feed类型，例如歌曲、歌单等，前台定义 **/
    private String feedType;

    /** 实际内容 **/
    private String content;

    /** 引用的feed id**/
    private FeedModel refFeed;

    /** 发表时间 **/
    private Date createDate;

    public FeedModel() {}
    public FeedModel(long id) {
        this.id = id;
    }

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

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FeedModel getRefFeed() {
        return refFeed;
    }

    public void setRefFeed(FeedModel refFeed) {
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
        return "FeedModel{" +
                "id=" + id +
                ", userid=" + userid +
                ", feedType='" + feedType + '\'' +
                ", content='" + content + '\'' +
                ", refFeed=" + refFeed +
                ", createDate=" + createDate +
                '}';
    }
}
