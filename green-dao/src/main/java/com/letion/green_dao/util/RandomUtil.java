package com.letion.green_dao.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *
 * @author wuqi
 * @describe ...
 * @date 2018/9/13 0013
 */
public class RandomUtil {
    public static String number11() {
        int start = (int) (Math.random() * 10000);
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("mmssSSS")
                .format(new Date());
        return String.valueOf(start) + date;
    }

    public static String number18() {
        int start = (int) (Math.random() * 10000);
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("yyyyMMddHHMMSS")
                .format(new Date());
        return String.valueOf(start) + date;
    }
}
