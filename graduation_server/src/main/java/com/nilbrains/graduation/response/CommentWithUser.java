package com.nilbrains.graduation.response;

import com.nilbrains.graduation.pojo.User;

import java.util.Date;
import java.util.List;

public class CommentWithUser {
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
    //    时间
    private Date publishTime;

    private String isTrue;

    public String getIsTrue() {
        return isTrue;
    }

    public void setIsTrue(String isTrue) {
        this.isTrue = isTrue;
    }

    private User user;

    public List<CommentWithUser> getChildren() {
        return children;
    }

    public void setChildren(List<CommentWithUser> children) {
        this.children = children;
    }

    private List<CommentWithUser> children;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
