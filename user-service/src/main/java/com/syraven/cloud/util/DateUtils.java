package com.syraven.cloud.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/5/29 13:20
 */
public class DateUtils {

    public static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";


    /**
     * 获得某天最小时间
     * 例如 ： 2020-05-29 00:00:00
     * @param date
     * @return
     */
    public static Date getStartOfDay(Date date){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalDateTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取某天最大时间
     * 例如 ： 2020-05-29 23:59:59
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date){
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalDateTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * util.Date转为sql.Date
     * @param date
     * @return
     */
    public static java.sql.Date utilConvert(Date date){
        Date utilDate = new Date(date.getTime());
        return new java.sql.Date(utilDate.getTime());
    }

    /**
     * sql.Date转为util.Date
     * @param date
     * @return
     */
    public static java.util.Date sqlCovertDate(java.sql.Date date){
        return new Date(date.getTime());
    }
}