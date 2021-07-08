package com.syraven.cloud;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.assertj.core.util.Lists;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * <<金额的计算BigDecimal类>>
 *
 * @author Raven
 * @date 2020/1/17 15:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BigDecimalTest {

    public static final String YYYYMMDD = "yyyy-MM-dd";
    public static final String YYYYMMDD_ZH = "yyyyMMdd";
    public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    public static final String YYYYMM = "yyyy-MM";

    public static void main(String[] args) throws ParseException {
        /*double d = 9.84;
        double d2 = 1.22;

        //注意需要使用BigDecimal(String val)构造方法
        BigDecimal bigDecimal = new BigDecimal(Double.toString(d));
        BigDecimal bigDecimal2 = new BigDecimal(Double.toString(d2));

        //加法
        BigDecimal bigDecimalAdd = bigDecimal.add(bigDecimal2);
        double add = bigDecimalAdd.doubleValue();

        //减法
        BigDecimal bigDecimalSubtraction = bigDecimal.subtract(bigDecimal2);
        double subtract = bigDecimalSubtraction.doubleValue();

        //乘法
        BigDecimal bigDecimalMultiply = bigDecimal.multiply(bigDecimal2);
        double multiply = bigDecimalMultiply.doubleValue();

        //除法
        int scale = 2;//保留2位小数
        BigDecimal bigDecimalDivide = bigDecimal.divide(bigDecimal2,scale,BigDecimal.ROUND_HALF_UP);
        double divide = bigDecimalDivide.doubleValue();

        System.out.println("d+d2 = " + add);
        System.out.println("d-d2 = " + subtract);
        System.out.println("d*d2 = " + multiply);
        System.out.println("d/d2 = " + divide);

        double format = 12343171.6;

        //获取常规数值格式
        NumberFormat number = NumberFormat.getNumberInstance();
        String str = number.format(format);
        System.out.println(str);

        //获取整数数值格式
        NumberFormat integer = NumberFormat.getIntegerInstance();
        str = integer.format(format);//如果带小数会四舍五入到整数12,343,172
        System.out.println(str);

        //获取货币数值格式
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        currency.setMinimumFractionDigits(2);//设置数的小数部分所允许的最小位数(如果不足后面补0)
        currency.setMaximumFractionDigits(4);//设置数的小数部分所允许的最大位数(如果超过会四舍五入)
        str = currency.format(format);//￥12,343,171.60
        System.out.println(str);

        //获取显示百分比的格式
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMinimumFractionDigits(2);//设置数的小数部分所允许的最小位数(如果不足后面补0)
        percent.setMaximumFractionDigits(3);//设置数的小数部分所允许的最大位数(如果超过会四舍五入)
        str = percent.format(format);
        System.out.println(str);*/
        /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         *//* Date myDate1 = sdf.parse("2020-5-25");*//*
        Date date = new Date();
        System.out.println(sdf.format(getEndOfDay(date)));*/

        String str_begin = "2019-12-15";
        String str_end = "2021-12-14";
        //getQuarter(str_begin,str_end);
        //getMonths(str_begin, str_end) ;
        getYears(str_begin, str_end);
        /*List<String> stringList = sliceUpDateRange(str_begin,str_end);
        for (String s : stringList) {
            System.out.println(s);
        }*/
       /* long val = 999999;
        System.out.println(insertRandomBitPer5Bits(val));

        String houseCode= "HZ01EZP476N42G";
        System.out.println(toBase10(houseCode));
        System.out.println(toBase62(val));*/

        List<Integer> oldList = Lists.newArrayList(1,2,3,4);
        List<Integer> newList = Lists.newArrayList(1,3,5,7);
        List<Integer> oldListCopy = Lists.newArrayList();
        oldListCopy.addAll(oldList);

        //获取被删除的标签
        oldList.removeAll(newList);

        //获取新增的标签
        newList.removeAll(oldListCopy);

        //删除id在oldList里的标签
        /*for (Integer integer : oldList) {
            System.out.println(integer);
        }
        //添加newList的标签

        for (Integer integer : newList) {
            System.out.println(integer);
        }*/

        oldListCopy.removeAll(oldList);
        oldListCopy.addAll(newList);
        for (Integer integer : oldListCopy) {
            System.out.println(integer);
        }



    }


    private static long insertRandomBitPer5Bits(long val) {
        long result = val;
        long high = val;
        for (int i = 0; i < 10; i++) {
            if (high == 0) {
                break;
            }
            int pos = 5 + 5 * i + i;
            high = result >> pos;
            result = ((high << 1 | RandomUtils.nextInt( 2)) << pos)
                    | (result & (-1L >>> (64 - pos)));
        }
        return result;
    }
    private static final String BASE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static long toBase10(String input) {
        int srcBase = BASE.length();
        long id = 0;
        String r = new StringBuilder(input).reverse().toString();

        for (int i = 0; i < r.length(); i++) {
            int charIndex = BASE.indexOf(r.charAt(i));
            id += charIndex * (long) Math.pow(srcBase, i);
        }

        return id;
    }

    public static String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        int targetBase = BASE.length();
        do {
            int i = (int) (num % targetBase);
            sb.append(BASE.charAt(i));
            num /= targetBase;
        } while (num > 0);

        return sb.reverse().toString();
    }



    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        ;
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }


    private static void getYears(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = new Date();
        Date end = new Date();
        try {
            begin = sdf.parse(startDate);
            end = sdf.parse(endDate);
        } catch (ParseException e) {
            System.out.println("日期输入格式不对");
            return;
        }
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(begin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(end);
        while (true) {

            if (calStart.get(Calendar.YEAR) == calEnd.get(Calendar.YEAR)) {
                /*System.out.println(sdf.format(cal_begin.getTime())+"~"+sdf.format(cal_end.getTime()));*/
                break;
            } else {
                String beginDate = sdf.format(calStart.getTime());
                String finishDate = sdf.format(calEnd.getTime());
                Date dateFinish = sdf.parse(finishDate);
                calStart.add(Calendar.YEAR, 1);
                calStart.add(Calendar.DATE, -1);
                String cutOffDate = sdf.format(calStart.getTime());
                /*int years=getYear(str_begin);
                String year=years+"-12"+"-31";*/
                Date dateBegin = sdf.parse(beginDate);
                Date dateOff = sdf.parse(cutOffDate);

                if (dateBegin.compareTo(dateOff) >= 0) {
                    break;
                } else {
                    if (dateOff.compareTo(dateFinish) >= 0) {
                        cutOffDate = sdf.format(dateFinish.getTime());
                    }
                    System.out.println(beginDate + "~" + cutOffDate);
                }
                /*cal_begin.add(Calendar.YEAR, 1);
                cal_begin.set(Calendar.DATE, 1);*/
                calStart.add(Calendar.DATE, 1);

            }

        }

    }

    private static void getQuarter(String begins, String ends) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = new Date();
        Date end = new Date();
        try {
            begin = sdf.parse(begins);
            end = sdf.parse(ends);
        } catch (ParseException e) {
            System.out.println("日期输入格式不对");
            return;
        }
        Calendar cal_begin = Calendar.getInstance();
        cal_begin.setTime(begin);
        Calendar cal_end = Calendar.getInstance();
        cal_end.setTime(end);
        while (true) {

            String str_begin = sdf.format(cal_begin.getTime());
            String str_end = getMonthEnd(cal_begin.getTime());
            Date begin_date = parseDate(str_end);
            Date end_date = parseDate(str_end);
            String Quarter_begin = formatDate(getFirstDateOfSeason(begin_date));
            String Quarter_end = formatDate(getLastDateOfSeason(end_date));
            Date Quarter_begin_date = parseDate(Quarter_begin);
            Date Quarter_end_date = parseDate(Quarter_end);


            if (Quarter_end_date.getTime() == end_date.getTime()) {

                if (Quarter_begin_date.getTime() <= begin.getTime()) {
                    Quarter_begin = begins;
                }
                if (Quarter_end_date.getTime() >= end.getTime()) {
                    Quarter_end = ends;
                }
                System.out.println(Quarter_begin + "~" + Quarter_end);
                if (end.getTime() <= end_date.getTime()) {
                    break;
                }
            } else if (Quarter_begin_date.getTime() == begin_date.getTime()) {
                if (Quarter_begin_date.getTime() <= begin.getTime()) {
                    Quarter_begin = begins;
                }
                if (Quarter_end_date.getTime() >= end.getTime()) {
                    Quarter_end = ends;
                }
                System.out.println(Quarter_begin + "~" + Quarter_end);
            }

            cal_begin.add(Calendar.MONTH, 1);
            cal_begin.set(Calendar.DAY_OF_MONTH, 1);
        }


    }


    private static void getMonths(String begins, String ends) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date begin = new Date();
        Date end = new Date();
        try {
            begin = sdf.parse(begins);
            end = sdf.parse(ends);
        } catch (ParseException e) {
            System.out.println("日期输入格式不对");
            return;
        }

        Calendar cal_begin = Calendar.getInstance();
        cal_begin.setTime(begin);
        Calendar cal_end = Calendar.getInstance();
        cal_end.setTime(end);
        while (true) {
            if (cal_begin.get(Calendar.YEAR) == cal_end.get(Calendar.YEAR) && cal_begin.get(Calendar.MONTH) == cal_end.get(Calendar.MONTH)) {
                System.out.println(sdf.format(cal_begin.getTime()) + "~" + sdf.format(cal_end.getTime()));
                break;
            }
            String str_begin = sdf.format(cal_begin.getTime());
            String str_end = getMonthEnd(cal_begin.getTime());
            System.out.println(str_begin + "~" + str_end);
            cal_begin.add(Calendar.MONTH, 1);
            cal_begin.set(Calendar.DAY_OF_MONTH, 1);
        }

    }

    private static void getWeeks(String begins, String ends) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdw = new SimpleDateFormat("E");
        String begin_date = begins;
        String end_date = ends;
        //String begin_date_fm = begin_date.substring(0, 4) + "-" + begin_date.substring(4,6) + "-" + begin_date.substring(6,8);
        // String end_date_fm =  end_date.substring(0, 4) + "-" + end_date.substring(4,6) + "-" + end_date.substring(6,8);
        String begin_date_fm = begins;
        String end_date_fm = ends;
        Date b = null;
        Date e = null;
        try {
            b = sd.parse(begin_date_fm);
            e = sd.parse(end_date_fm);
        } catch (ParseException ee) {
            ee.printStackTrace();
        }
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(b);
        Date time = b;
        String year = begin_date_fm.split("-")[0];
        String mon = Integer.parseInt(begin_date_fm.split("-")[1]) < 10 ? begin_date_fm.split("-")[1] : begin_date_fm.split("-")[1];
        String day = Integer.parseInt(begin_date_fm.split("-")[2]) < 10 ? begin_date_fm.split("-")[2] : begin_date_fm.split("-")[2];
        String timeb = year + mon + day;
        String timee = null;
        if (begin_date == end_date) {
            System.out.println(begin_date + "~" + end_date);
        } else {
            while (time.getTime() <= e.getTime()) {
                rightNow.add(Calendar.DAY_OF_YEAR, 1);
                time = sd.parse(sd.format(rightNow.getTime()));
                if (time.getTime() > e.getTime()) {
                    break;
                }
                String timew = sdw.format(time);
                if (("星期一").equals(timew)) {
                    timeb = (sd.format(time)).replaceAll("-", "");
                }
                if (("星期日").equals(timew) || ("星期七").equals(timew) || time.getTime() == e.getTime()) {
                    timee = (sd.format(time)).replaceAll("-", "");
                    String begindate = fomaToDatas(timeb);
                    String enddate = fomaToDatas(timee);
                    System.out.println(begindate + "~" + enddate);
                }
            }

        }
    }


    public static String fomaToDatas(String data) {
        DateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        try {
            Date parse = fmt.parse(data);
            DateFormat fmt2 = new SimpleDateFormat("yyyy-MM-dd");
            return fmt2.format(parse);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }


    }


    /**
     * 日期解析
     *
     * @param strDate
     * @param pattern
     * @return
     */
    public static Date parseDate(String strDate, String pattern) {
        Date date = null;
        try {
            if (pattern == null) {
                pattern = YYYYMMDD;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            date = format.parse(strDate);
        } catch (Exception e) {

        }
        return date;
    }

    public static int getYear(String date) throws ParseException {
        Calendar c = Calendar.getInstance();
        c.setTime(parseDate(date));
        int year = c.get(Calendar.YEAR);
        return year;
    }

    public String getYearMonth(Date date) {
        return formatDateByFormat(date, "yyyy-MM");
    }

    /**
     * 取得指定月份的第一天
     *
     * @param strdate String
     * @return String
     */
    public String getMonthBegin(Date date) {
        return formatDateByFormat(date, "yyyy-MM") + "-01";
    }

    /**
     * 取得指定月份的最后一天
     *
     * @param strdate String
     * @return String
     */
    public static String getMonthEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return formatDateByFormat(calendar.getTime(), "yyyy-MM-dd");
    }

    /**
     * 以指定的格式来格式化日期
     *
     * @param date   Date
     * @param format String
     * @return String
     */
    public static String formatDateByFormat(Date date, String format) {
        String result = "";
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * @param strDate
     * @return
     */
    public static Date parseDate(String strDate) {
        return parseDate(strDate, null);
    }


    /**
     * format date
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return formatDate(date, null);
    }

    /**
     * format date
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        String strDate = null;
        try {
            if (pattern == null) {
                pattern = YYYYMMDD;
            }
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            strDate = format.format(date);
        } catch (Exception e) {

        }
        return strDate;
    }

    /**
     * 取得日期：年
     *
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int year = c.get(Calendar.YEAR);
        return year;
    }

    /**
     * 取得日期：年
     *
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        return month + 1;
    }

    /**
     * 取得日期：年
     *
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int da = c.get(Calendar.DAY_OF_MONTH);
        return da;
    }

    /**
     * 取得当天日期是周几
     *
     * @param date
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week_of_year = c.get(Calendar.DAY_OF_WEEK);
        return week_of_year - 1;
    }

    /**
     * 取得一年的第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week_of_year = c.get(Calendar.WEEK_OF_YEAR);
        return week_of_year;
    }

    /**
     * getWeekBeginAndEndDate
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getWeekBeginAndEndDate(Date date, String pattern) {
        Date monday = getMondayOfWeek(date);
        Date sunday = getSundayOfWeek(date);
        return formatDate(monday, pattern) + " - "
                + formatDate(sunday, pattern);
    }

    /**
     * 根据日期取得对应周周一日期
     *
     * @param date
     * @return
     */
    public static Date getMondayOfWeek(Date date) {
        Calendar monday = Calendar.getInstance();
        monday.setTime(date);
        monday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return monday.getTime();
    }

    /**
     * 根据日期取得对应周周日日期
     *
     * @param date
     * @return
     */
    public static Date getSundayOfWeek(Date date) {
        Calendar sunday = Calendar.getInstance();
        sunday.setTime(date);
        sunday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
        sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return sunday.getTime();
    }

    /**
     * 取得月的剩余天数
     *
     * @param date
     * @return
     */
    public static int getRemainDayOfMonth(Date date) {
        int dayOfMonth = getDayOfMonth(date);
        int day = getPassDayOfMonth(date);
        return dayOfMonth - day;
    }

    /**
     * 取得月已经过的天数
     *
     * @param date
     * @return
     */
    public static int getPassDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得月天数
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 取得月第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得月最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 取得季度第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDateOfSeason(Date date) {
        return getFirstDateOfMonth(getSeasonDate(date)[0]);
    }

    /**
     * 取得季度最后一天
     *
     * @param date
     * @return
     */
    public static Date getLastDateOfSeason(Date date) {
        return getLastDateOfMonth(getSeasonDate(date)[2]);
    }

    /**
     * 取得季度天数
     *
     * @param date
     * @return
     */
    public static int getDayOfSeason(Date date) {
        int day = 0;
        Date[] seasonDates = getSeasonDate(date);
        for (Date date2 : seasonDates) {
            day += getDayOfMonth(date2);
        }
        return day;
    }

    /**
     * 取得季度剩余天数
     *
     * @param date
     * @return
     */
    public static int getRemainDayOfSeason(Date date) {
        return getDayOfSeason(date) - getPassDayOfSeason(date);
    }

    /**
     * 取得季度已过天数
     *
     * @param date
     * @return
     */
    public static int getPassDayOfSeason(Date date) {
        int day = 0;

        Date[] seasonDates = getSeasonDate(date);

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);

        if (month == Calendar.JANUARY || month == Calendar.APRIL
                || month == Calendar.JULY || month == Calendar.OCTOBER) {// 季度第一个月
            day = getPassDayOfMonth(seasonDates[0]);
        } else if (month == Calendar.FEBRUARY || month == Calendar.MAY
                || month == Calendar.AUGUST || month == Calendar.NOVEMBER) {// 季度第二个月
            day = getDayOfMonth(seasonDates[0])
                    + getPassDayOfMonth(seasonDates[1]);
        } else if (month == Calendar.MARCH || month == Calendar.JUNE
                || month == Calendar.SEPTEMBER || month == Calendar.DECEMBER) {// 季度第三个月
            day = getDayOfMonth(seasonDates[0]) + getDayOfMonth(seasonDates[1])
                    + getPassDayOfMonth(seasonDates[2]);
        }
        return day;
    }

    /**
     * 取得季度月
     *
     * @param date
     * @return
     */
    public static Date[] getSeasonDate(Date date) {
        Date[] season = new Date[3];

        Calendar c = Calendar.getInstance();
        c.setTime(date);

        int nSeason = getSeason(date);
        if (nSeason == 1) {// 第一季度
            c.set(Calendar.MONTH, Calendar.JANUARY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.FEBRUARY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MARCH);
            season[2] = c.getTime();
        } else if (nSeason == 2) {// 第二季度
            c.set(Calendar.MONTH, Calendar.APRIL);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.MAY);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.JUNE);
            season[2] = c.getTime();
        } else if (nSeason == 3) {// 第三季度
            c.set(Calendar.MONTH, Calendar.JULY);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.AUGUST);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.SEPTEMBER);
            season[2] = c.getTime();
        } else if (nSeason == 4) {// 第四季度
            c.set(Calendar.MONTH, Calendar.OCTOBER);
            season[0] = c.getTime();
            c.set(Calendar.MONTH, Calendar.NOVEMBER);
            season[1] = c.getTime();
            c.set(Calendar.MONTH, Calendar.DECEMBER);
            season[2] = c.getTime();
        }
        return season;
    }

    /**
     * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
     *
     * @param date
     * @return
     */
    public static int getSeason(Date date) {

        int season = 0;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.FEBRUARY:
            case Calendar.MARCH:
                season = 1;
                break;
            case Calendar.APRIL:
            case Calendar.MAY:
            case Calendar.JUNE:
                season = 2;
                break;
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.SEPTEMBER:
                season = 3;
                break;
            case Calendar.OCTOBER:
            case Calendar.NOVEMBER:
            case Calendar.DECEMBER:
                season = 4;
                break;
            default:
                break;
        }
        return season;
    }


    /**
     * 日期正则表达式
     */
    public static String YEAR_REGEX = "^\\d{4}$";
    public static String MONTH_REGEX = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}$";
    public static String DATE_REGEX = "^\\d{4}(\\-|\\/|\\.)\\d{1,2}\\1\\d{1,2}$";

    /**
     * 格式化日期
     * - yyyy-MM-dd HH:mm:ss
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        return sd.format(date);
    }

    /**
     * 格式化日期
     * - yyyy-MM-dd HH:mm:ss
     *
     * @param date    日期字符串
     * @param pattern 日期格式
     * @return 日期
     * @throws ParseException 解析异常
     */
    public static Date parse(String date, String pattern) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        try {
            return sd.parse(date);
        } catch (ParseException e) {
            throw e;
        }
    }

    /**
     * 日期范围 - 切片
     * <pre>
     * -- eg:
     * 年 ----------------------- sliceUpDateRange("2018", "2020");
     * rs: [2018, 2019, 2020]
     *
     * 月 ----------------------- sliceUpDateRange("2018-06", "2018-08");
     * rs: [2018-06, 2018-07, 2018-08]
     *
     * 日 ----------------------- sliceUpDateRange("2018-06-30", "2018-07-02");
     * rs: [2018-06-30, 2018-07-01, 2018-07-02]
     * </pre>
     *
     * @param startDate 起始日期
     * @param endDate   结束日期
     * @return 切片日期
     */
    public static List<String> sliceUpDateRange(String startDate, String endDate) {
        List<String> rs = new ArrayList<>();
        try {
            int dt = Calendar.DATE;
            String pattern = "yyyy-MM-dd";
            if (startDate.matches(DATE_REGEX)) {
                pattern = "yyyy-MM-dd";
                dt = Calendar.YEAR;
            } else if (startDate.matches(MONTH_REGEX)) {
                pattern = "yyyy-MM";
                dt = Calendar.MONTH;
            } else if (startDate.matches(DATE_REGEX)) {
                pattern = "yyyy-MM-dd";
                dt = Calendar.DATE;
            }
            Calendar sc = Calendar.getInstance();
            Calendar ec = Calendar.getInstance();
            sc.setTime(parse(startDate, pattern));
            ec.setTime(parse(endDate, pattern));
            while (sc.compareTo(ec) < 1) {
                rs.add(format(sc.getTime(), pattern));
                sc.add(dt, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rs;
    }

}