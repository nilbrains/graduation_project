package com.nilbrains.graduation.service.impl;

import com.nilbrains.graduation.dao.PostMapper;
import com.nilbrains.graduation.dao.SettingMapper;
import com.nilbrains.graduation.dao.UserMapper;
import com.nilbrains.graduation.pojo.Setting;
import com.nilbrains.graduation.response.ResponseResult;
import com.nilbrains.graduation.service.ISettingService;
import com.nilbrains.graduation.utils.Constants;
import com.nilbrains.graduation.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
public class SettingService implements ISettingService {

    @Autowired
    private SettingMapper settingMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PostMapper postMapper;

    @Override
    public ResponseResult setStatement(Setting setting) {
        checkInitSetting();
        if (TextUtils.isEmpty(setting.getValue())) {
            return ResponseResult.FAILED("设置内容不能为空");
        }

        setting.setKey(Constants.Setting.WEB_STATEMENT);

        settingMapper.updateSetting(setting);

        return ResponseResult.SUCCESS("设置成功");
    }

    @Override
    public ResponseResult getStatement() {
        checkInitSetting();
        Setting setting = settingMapper.querySettingByKey(Constants.Setting.WEB_STATEMENT);
        return ResponseResult.SUCCESS("获取成功").setData(setting);
    }

    @Override
    public ResponseResult getAll() {
        checkInitSetting();
        List<Setting> settings = new ArrayList<>();

        settings.add(settingMapper.querySettingByKey(Constants.Setting.WEB_STATEMENT));
        settings.add(settingMapper.querySettingByKey(Constants.Setting.WEB_SITE_UP_TIME));
        settings.add(settingMapper.querySettingByKey(Constants.Setting.WEB_SITE_RECORD));
//        TODO

        return ResponseResult.SUCCESS("获取成功").setData(settings);
    }

    @Override
    public ResponseResult setSiteUpTime(Setting setting) {
        checkInitSetting();
        if (TextUtils.isEmpty(setting.getValue())) {
            return ResponseResult.FAILED("设置内容不能为空");
        }

        setting.setKey(Constants.Setting.WEB_SITE_UP_TIME);

        settingMapper.updateSetting(setting);

        return ResponseResult.SUCCESS("设置成功");
    }

    @Override
    public ResponseResult setSiteRecord(Setting setting) {
        checkInitSetting();
        if (TextUtils.isEmpty(setting.getValue())) {
            return ResponseResult.FAILED("设置内容不能为空");
        }

        setting.setKey(Constants.Setting.WEB_SITE_RECORD);

        settingMapper.updateSetting(setting);

        return ResponseResult.SUCCESS("设置成功");
    }

    @Override
    public ResponseResult getWebInfo() {
        checkInitSetting();
//        获取站点信息
        Map<String, Object> resultMap = new HashMap<>();

        Setting siteUpTime = settingMapper.querySettingByKey(Constants.Setting.WEB_SITE_UP_TIME);
        resultMap.put(siteUpTime.getKey(), siteUpTime.getValue());
        Setting siteRecord = settingMapper.querySettingByKey(Constants.Setting.WEB_SITE_RECORD);
        resultMap.put(siteRecord.getKey(), siteRecord.getValue());

        int countUser = userMapper.getCount(Constants.Status.NULL);
        int countArticle = postMapper.getCount(Constants.Status.ARTICLE);
        int countAnswer = postMapper.getCount(Constants.Status.ANSWER);
        resultMap.put("user_count", countUser);
        resultMap.put("article_count", countArticle);
        resultMap.put("answer_count", countAnswer);

        return ResponseResult.SUCCESS("获取成功").setData(resultMap);
    }

    private void checkInitSetting() {
//        setting 初始化
        Setting settingInit = settingMapper.querySettingByKey(Constants.Setting.SETTING_INIT);
        if (settingInit == null) {
//            没有初始化
            settingMapper.saveSettingKeyAndValue(Constants.Setting.SETTING_INIT, "1");
            settingMapper.saveSettingKeyAndValue(Constants.Setting.WEB_STATEMENT, "");
            settingMapper.saveSettingKeyAndValue(Constants.Setting.WEB_SITE_UP_TIME, "");
            settingMapper.saveSettingKeyAndValue(Constants.Setting.WEB_SITE_RECORD, "");
//        TODO
        }
    }


}
