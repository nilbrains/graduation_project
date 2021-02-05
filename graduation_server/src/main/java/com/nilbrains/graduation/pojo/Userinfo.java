package com.nilbrains.graduation.pojo;

import java.util.Date;

public class Userinfo {
    //    id
    private String id;
    //    用户id
    private String uid;
    //    签到天数
    private Integer days;
    //    最后签到日期
    private Date signDay;
    //    pyb
    private Integer pyb;
    //    等级
    private String rate;
    //    积分
    private Integer integral;

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

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Date getSignDay() {
        return signDay;
    }

    public void setSignDay(Date signDay) {
        this.signDay = signDay;
    }

    public Integer getPyb() {
        return pyb;
    }

    public void setPyb(Integer pyb) {
        this.pyb = pyb;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }
}
