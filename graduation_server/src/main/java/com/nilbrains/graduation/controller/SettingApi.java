package com.nilbrains.graduation.controller;

import com.nilbrains.graduation.pojo.Setting;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.ISettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SettingApi {

    @Autowired
    private ISettingService settingService;

    //      获取ALL
    @GetMapping("/setting/all")
    public ResponseResult getAll() {
        return settingService.getAll();
    }


    //      获取社区声明
    @GetMapping("/setting/statement")
    public ResponseResult getStatement() {
        return settingService.getStatement();
    }

    //      设置社区声明
    @PostMapping("/setting/statement")
    public ResponseResult setStatement(
            @RequestBody Setting setting
    ) {
        return settingService.setStatement(setting);
    }

    @PostMapping("/setting/site-up-time")
    public ResponseResult setSiteUpTime(
            @RequestBody Setting setting
    ) {
        return settingService.setSiteUpTime(setting);
    }

    @PostMapping("/setting/site-record")
    public ResponseResult setSiteRecord(
            @RequestBody Setting setting
    ) {
        return settingService.setSiteRecord(setting);
    }

    @GetMapping("/setting/web-info")
    public ResponseResult getWebInfo() {
        return settingService.getWebInfo();
    }

}
