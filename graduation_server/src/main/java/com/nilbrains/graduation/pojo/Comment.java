package com.nilbrains.graduation.pojo;

import java.util.Date;

/**
 * 评论
 */
public class Comment {
    //    id
    private String id;
    //    用户id
    private String uid;
    //    评论内容
    private String content;
    //    父评论id
    private String tid;
    //    文章id
    private String pid;
    //    是否正确
    private String isTrue;

    public String getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(String isTrue) {
        this.isTrue = isTrue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    //    时间
    private Date publishTime;
}
