package com.vinci.backend.domain.relations.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 一个关注
 * Created by tim@vinci on 15-1-30.
 */
public class Attention implements Serializable{

    public Attention() {
    }

    private long id;

    private long sourceUserId;

    private long dstUserId;

    private Date createDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(long sourceUserId) {
        this.sourceUserId = sourceUserId;
    }

    public long getDstUserId() {
        return dstUserId;
    }

    public void setDstUserId(long dstUserId) {
        this.dstUserId = dstUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Attention{" +
                ", id=" + id +
                ", sourceUserId=" + sourceUserId +
                ", dstUserId=" + dstUserId +
                ", createDate=" + createDate +
                '}';
    }
}
