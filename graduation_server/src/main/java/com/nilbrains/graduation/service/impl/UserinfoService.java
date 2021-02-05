package com.nilbrains.graduation.service.impl;

import com.nilbrains.graduation.dao.UserinfoMapper;
import com.nilbrains.graduation.pojo.User;
import com.nilbrains.graduation.pojo.Userinfo;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.IUserService;
import com.nilbrains.graduation.service.IUserinfoService;
import com.nilbrains.graduation.utils.Constants;
import com.nilbrains.graduation.utils.IdWorker;
import com.nilbrains.graduation.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional
public class UserinfoService implements IUserinfoService {

    @Autowired
    private UserinfoMapper userinfoMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private IdWorker idWorker;

    @Override
    public void initUserInfo(String uid) {
//        初始化用户信息
        Userinfo userinfo = new Userinfo();
        userinfo.setId(String.valueOf(idWorker.nextId()));
        userinfo.setUid(uid);
        userinfo.setDays(0);
//        初始化 2积分 2pyb
        userinfo.setPyb(Constants.Userinfo.PY_2);
        userinfo.setRate(Constants.Userinfo.LV_0);
        userinfo.setIntegral(Constants.Userinfo.PY_2);
        userinfoMapper.save(userinfo);
    }

    @Override
    public void addPyb(String uid, int pyb) {
        Userinfo userinfo = getUserinfo(uid);
        int pyb2db = userinfo.getPyb() + pyb;
        userinfo.setPyb(pyb2db);
        userinfoMapper.update(userinfo);
    }

    @Override
    public void addIntegral(String uid, int integral) {
        Userinfo userinfo = getUserinfo(uid);
        int integral2db = userinfo.getIntegral() + integral;

        if (integral2db >= Constants.Userinfo.INTEGRAL_IF_6) {
            userinfo.setRate(Constants.Userinfo.LV_6);
        } else if (integral2db >= Constants.Userinfo.INTEGRAL_IF_5) {
            userinfo.setRate(Constants.Userinfo.LV_5);
        } else if (integral2db >= Constants.Userinfo.INTEGRAL_IF_4) {
            userinfo.setRate(Constants.Userinfo.LV_4);
        } else if (integral2db >= Constants.Userinfo.INTEGRAL_IF_3) {
            userinfo.setRate(Constants.Userinfo.LV_3);
        } else if (integral2db >= Constants.Userinfo.INTEGRAL_IF_2) {
            userinfo.setRate(Constants.Userinfo.LV_2);
        } else if (integral2db >= Constants.Userinfo.INTEGRAL_IF_1) {
            userinfo.setRate(Constants.Userinfo.LV_1);
        }

        userinfo.setIntegral(integral2db);
        userinfoMapper.update(userinfo);
    }

    @Override
    public ResponseResult getUserInfoByUid() {
        User user = userService.checkUser();
        Userinfo userinfo = getUserinfo(user.getUid());
        return ResponseResult.SUCCESS("获取成功").setData(userinfo);
    }

    @Override
    public ResponseResult userSignIn() {
//        签到
        User user = userService.checkUser();

        String uid = user.getUid();

        Userinfo userinfoDB = this.getUserinfo(uid);

//        获取当前日期
        Date nowDate = new Date();
        if (TextUtils.isSameDate(nowDate, userinfoDB.getSignDay())) {
            return ResponseResult.FAILED("今日已签到");
        }

        userinfoDB.setSignDay(nowDate);
        userinfoDB.setDays(userinfoDB.getDays() + 1);
        userinfoDB.setIntegral(userinfoDB.getIntegral() + Constants.Userinfo.PY_4);
        userinfoDB.setPyb(userinfoDB.getPyb() + Constants.Userinfo.PY_1);

        userinfoMapper.update(userinfoDB);
        return ResponseResult.SUCCESS("签到成功");
    }

    public Userinfo getUserinfo(String uid) {
        Userinfo userinfo = userinfoMapper.findOneByUid(uid);
        log.debug("userinfo =======> " + (userinfo == null));
        if (userinfo == null) {
            this.initUserInfo(uid);
        }
        return userinfoMapper.findOneByUid(uid);
    }
}
