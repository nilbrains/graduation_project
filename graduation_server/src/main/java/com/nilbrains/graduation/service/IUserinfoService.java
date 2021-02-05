package com.nilbrains.graduation.service;

import com.nilbrains.graduation.pojo.Userinfo;
import com.nilbrains.graduation.response.ResponseResult;

public interface IUserinfoService {

    //    初始化用户信息
    void initUserInfo(String uid);

    //    添加pyb
    void addPyb(String uid, int pyb);

    //    添加积分
    void addIntegral(String uid, int integral);

    // 获取用户其它信息
    //    积分 等级.....
    Userinfo getUserinfo(String uid);
    
    ResponseResult getUserInfoByUid();

    //    签到
    ResponseResult userSignIn();
}
