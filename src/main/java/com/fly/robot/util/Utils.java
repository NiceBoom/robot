package com.fly.robot.util;

import java.util.regex.Pattern;

/**
 * 工具类合集
 */
public class Utils {
    //校验是否为中国手机号
    public static boolean isChinaPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^1[3456789]\\d{9}$");
        return pattern.matcher(phoneNumber).matches();
    }
}
