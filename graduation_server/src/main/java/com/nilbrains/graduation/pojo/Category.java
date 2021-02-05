package com.nilbrains.graduation.pojo;

/**
 * 文章、问题分类
 */
public class Category {
    private String cid;
    private String title;
    private String status;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
