package com.iqiongzhi.SCB.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    /*
    提取用户信息
     */
    public static String getContext(String validationResult, String tag){
        String p = ".*<" + tag + ">(.*)</" + tag + ">.*";
        Pattern pattern = Pattern.compile(p);
        Matcher matcher = pattern.matcher(validationResult);
        if (matcher.find()) {
            return matcher.group(1);
        }else return "null";
    }
}
