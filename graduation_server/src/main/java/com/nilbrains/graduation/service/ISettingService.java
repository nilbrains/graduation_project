package com.nilbrains.graduation.service;

import com.nilbrains.graduation.pojo.Setting;
import com.nilbrains.graduation.response.ResponseResult;

public interface ISettingService {

    ResponseResult setStatement(Setting setting);

    ResponseResult getStatement();

    ResponseResult getAll();

    ResponseResult setSiteUpTime(Setting setting);

    ResponseResult setSiteRecord(Setting setting);

    ResponseResult getWebInfo();
}
