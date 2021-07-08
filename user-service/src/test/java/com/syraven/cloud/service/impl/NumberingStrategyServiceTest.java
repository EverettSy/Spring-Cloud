package com.syraven.cloud.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NumberingStrategyServiceTest {

    @Autowired
    private NumberingStrategyService numberingStrategyService;

    @Autowired
    private ContractNumberingStrategyService contractNumberingStrategyService;

    @Test
    public void getCode() {
        /*String ownerCode = numberingStrategyService.getCode("FD","0571");
        String houseCode = numberingStrategyService.getCode("HS","0571");
        String customerCode = numberingStrategyService.getCode("ZK","0571");
        String wtCode = numberingStrategyService.getCode("WTQD","0571");
        String czCode = numberingStrategyService.getCode("CZQD","0571");
        String czDCode = numberingStrategyService.getCode("CZ-D","0571");

        System.out.println(ownerCode);
        System.out.println(houseCode);
        System.out.println(customerCode);
        System.out.println(wtCode);
        System.out.println(czCode);*/

        /*String reg = "^[^\\s]*$";
        String str = "ab ddddc";
        System.out.println(str.matches(reg));
        if (!str.matches(reg)){
            System.out.println("合同编号不允许有空格");
        }*/
        /*String re = "黑龙江省";
        int se = re.lastIndexOf("省");
        System.out.println(se);
        String res = re.substring(0, re.indexOf("省")) + "省";
        System.out.println(res);

        String czDCode = numberingStrategyService.getCode("CZJG","0571");
        System.out.println(czDCode);*/
       /* String czDCode = numberingStrategyService.getCode("HS","0574");
        System.out.println(czDCode);*/

        /*String czDCode = numberingStrategyService.getCode("HZCK-CZ", "0512");
        System.out.println(czDCode);*/

       /* String czDCode = contractNumberingStrategyService.getContractNumCode("HZCK-CZ", "021","ck2");
        System.out.println(czDCode);*/
        /*Date date = new Date();

        Calendar calendarOld = new GregorianCalendar();
        calendarOld.setTime(date);
        calendarOld.add(Calendar.MONTH, 0);
        calendarOld.set(Calendar.HOUR, 16);
        calendarOld.set(Calendar.MINUTE, 0);
        Date oldDate = calendarOld.getTime();

        // 对日期的操作,我们需要使用 Calendar 对象

        Date appointmentDate;

        if (date.after(oldDate)){
            Calendar calendars = new GregorianCalendar();
            calendars.setTime(date);
            calendars.add(Calendar.MONTH, 0);
            calendars.add(Calendar.DAY_OF_MONTH, +1);
            calendars.set(Calendar.HOUR, 16);
            calendars.set(Calendar.MINUTE, 0);
            appointmentDate = calendars.getTime();
        }else {
            Calendar calendar1 = new GregorianCalendar();
            calendar1.setTime(date);
            calendar1.add(Calendar.MONTH, 0);
            // +1天
            calendar1.add(Calendar.DAY_OF_MONTH, +1);
            appointmentDate = calendar1.getTime();
        }

        System.out.println(appointmentDate);*/

        //云丁时间规则
       /* Date date = new Date();
        Date s = getIncomeDate(date);
        Long SS = s.getTime();
        System.out.println(SS);*/

       /* String url = "deliveriesSignid=23";
        Integer sd = url.split("deliveriesSign").length;
        System.out.println(sd);
        Integer id = Integer.valueOf(url.split("id=")[1].split("#/")[0]);
        System.out.println(id);*/

        /*String str = "";
        List<String> stringList = new ArrayList<>();
        stringList.add("TOBEDELIVERY");
        stringList.add("AUDITED");
        System.out.println(stringList);
        for (String s : stringList) {
            str += "'" + s + "',";
        }
        *//* str = StringUtils.join(stringList.toArray(), ",");*//*
        str = str.substring(0, str.length() - 1);
        System.out.println(str);
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>() {{
            put(1, 1);
            put(2, 2);
        }};
        List<User> arrayList = new ArrayList<>();
        for (Map.Entry<Object,Object> entry : hashMap.entrySet()){
            User user = new User();
            user.setUsername(entry.getKey().toString());
            user.setPassword(entry.getValue().toString());
            System.out.println(entry.getKey()+":"+entry.getValue());
            arrayList.add(user);
        }
        System.out.println(arrayList);
        System.out.println(hashMap);*/
        /*java.sql.Date date = DateUtils.utilConvert(new Date(System.currentTimeMillis()));
        System.out.println(date);

        Date date1 = DateUtils.sqlCovertDate(new java.sql.Date(System.currentTimeMillis()));
        System.out.println(date1.getTime());

        Date date2 = new Date();
        System.out.println(date2.getTime());*/

        String at = "12A";
        int b = 0;
        try {
            b = Integer.valueOf(at).intValue();
        } catch (NumberFormatException e) {
            //at = at.replaceAll("[A-Za-z]","");
            b = Integer.parseInt(at.replaceAll("[A-Za-z]","")) + 1;
            System.out.println(at);
            System.out.println(b);
        }

    }


    public static Date getIncomeDate(Date date) throws NullPointerException {
        if (null == date) {
            throw new NullPointerException("the date is null or empty!");
        }
        Calendar calendarOld = new GregorianCalendar();
        calendarOld.setTime(date);
        calendarOld.add(Calendar.MONTH, 0);
        calendarOld.set(Calendar.HOUR_OF_DAY, 16);
        calendarOld.set(Calendar.MINUTE, 0);
        Date oldDate = calendarOld.getTime();

        // 对日期的操作,我们需要使用 Calendar 对象

        Date appointmentDate;

        if (date.after(oldDate)) {
            Calendar calendars = new GregorianCalendar();
            calendars.setTime(date);
            calendars.add(Calendar.MONTH, 0);
            calendars.add(Calendar.DAY_OF_MONTH, +1);
            calendars.set(Calendar.HOUR_OF_DAY, 16);
            calendars.set(Calendar.MINUTE, 0);
            appointmentDate = calendars.getTime();
        } else {
            Calendar calendar1 = new GregorianCalendar();
            calendar1.setTime(date);
            calendar1.add(Calendar.MONTH, 0);
            // +1天
            calendar1.add(Calendar.DAY_OF_MONTH, +1);
            appointmentDate = calendar1.getTime();
        }
        return appointmentDate;
    }

    public static void main(String[] args) {
        String timestamp = "2021-05-20 12:18:15";
        long res = Long.valueOf(timestamp).longValue();
        long reqeustInterval = System.currentTimeMillis();
        System.out.println(res+reqeustInterval);
    }
}