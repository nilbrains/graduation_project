package com.nilbrains.graduation.utils;

public interface Constants {
    interface Userinfo {
        int PY_1 = 1;
        int PY_2 = 2;
        int PY_4 = 4;
        int PY_6 = 6;
        int PY_8 = 8;
        int PY_9 = 9;
        String LV_0 = "0";
        String LV_1 = "1";
        String LV_2 = "2";
        String LV_3 = "3";
        String LV_4 = "4";
        String LV_5 = "5";
        String LV_6 = "6";
        int INTEGRAL_IF_1 = 10;
        int INTEGRAL_IF_2 = 1000;
        int INTEGRAL_IF_3 = 5000;
        int INTEGRAL_IF_4 = 10000;
        int INTEGRAL_IF_5 = 50000;
        int INTEGRAL_IF_6 = 100000;
    }

    interface Status {
        //        活动
        String ACTIVE = "0";
        //        不活动
        String INACTIVE = "1";

        String ARTICLE = "0";

        String ANSWER = "1";
        Integer ZERO = 0;
        String NULL = "";
        String NUM_5 = "5";
        String FALSE = "0";
        String TRUE = "1";
    }

    interface TimeValue {
        int MIN = 60;
        int HOUR = MIN * 60;
        int HOUR_2 = 2 * HOUR;
        int DAY = 24 * HOUR;
        long MONTH = 30 * DAY;
    }

    interface User {
        //        管理员
        String ROLE_ADMIN_USER = "0";
        //        普通用户
        String ROLE_USER = "1";
        String DEFAULT_AVATAR = "https://i.loli.net/2020/05/07/YeHwMu3GkX276vp.png";
        String DEFAULT_SIGN = "这个人很懒，没有签名~~";
    }

    interface Setting {
        String MANAGE_ACCOUNT_INIT_STATE = "manage_account_init_state";
        String HEADER_AUTH = "auth";
        String G_PATH = "http://localhost:8080/image/";
        String WEB_STATEMENT = "web_statement";

        String SETTING_INIT = "setting_init";
        String WEB_SITE_UP_TIME = "web_site_up_time";
        String WEB_SITE_RECORD = "web_site_record";
    }

    /**
     * 图片类型
     */
    interface ImageType {
        String prefix = "image/";
        String TYPE_JPG = "jpg";
        String TYPE_PNG = "png";
        String TYPE_GIF = "gif";
        String TYPE_JPG_WITH_PREFIX = prefix + "jpg";
        String TYPE_PNG_WITH_PREFIX = prefix + "png";
        String TYPE_GIF_WITH_PREFIX = prefix + "gif";
        String TYPE_JPEG_WITH_PREFIX = prefix + "jpeg";
    }
}
