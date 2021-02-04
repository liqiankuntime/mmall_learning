package com.mmall2.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * @Version 1.0
 * @Author:liqiankun
 * @Date:2021/2/2
 * @Content:
 */
public class DateTimeUtil {
    // 使用开源包 joda-time来处理时间
    // str -> Date
    // Date -> str

    private static final String STANDARD_FORMAT = "yy-mm-dd hh:mm:ss";

    public static Date strToDate(String dateTimeStr, String formatStr ){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formatStr);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date, String formatStr){
        if(date == null){
            return "";
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(formatStr);
    }



    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

    public static String dateToStr(Date date){
        if(date == null){
            return "";
        }
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }




//    public static void main(String[] args) {//快捷键 psvm
//        System.out.println(new Date());//快捷键 sout
//
//
//        System.out.println(DateTimeUtil.dateToStr(new Date(), "yy-mm-dd hh:mm:ss"));
//
//        System.out.println(DateTimeUtil.strToDate("21-06-02 08:07:37", "yy-mm-dd hh:mm:ss"));
//    }

}
