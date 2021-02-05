package com.nilbrains.graduation.utils;

import java.util.Calendar;
import java.util.Date;

public class TextUtils {
    public static boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    public static boolean isSameDate(Date d1, Date d2) {
        if (null == d1 || null == d2)
            return false;

        if (Math.abs(d1.getTime() - d2.getTime()) > 3600000 * 24)
            return false;

        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d2);
        return c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }
}
