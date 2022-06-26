package com.syraven.cloud.service.impl;

import com.google.common.collect.Lists;
import com.syraven.cloud.common.constants.Constants;
import com.syraven.cloud.domain.Phone;
import com.syraven.cloud.service.PhoneService;
import com.syraven.cloud.vo.DynamicVO;
import com.syraven.cloud.vo.PhoneVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.commands.JedisCommands;

import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.*;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-06-25 17:16
 */
@Slf4j
@Service
public class PhoneServiceImpl implements PhoneService {

    List<Phone> phones = Lists.newArrayList(
            new Phone(1, "苹果"),
            new Phone(2, "小米"),
            new Phone(3, "华为"),
            new Phone(4, "一加"),
            new Phone(5, "vivo"));

    @Autowired
    private JedisCommands jedis;

    @Override
    public void buyPhone(int phoneId) {
        // 购买成功则对排行榜中该手机的销量进行加1
        jedis.zincrby(Constants.SALES_LIST, 1, String.valueOf(phoneId));
        // 添加购买动态
        Clock clock = Clock.systemDefaultZone();
        String msg = clock.millis() + Constants.separator + phones.get(phoneId - 1).getName();
        jedis.lpush(Constants.BUY_DYNAMIC, msg);
    }

    @Override
    public List<PhoneVO> getPhbList() {
        // 按照销量多少排行，取出前五名
        Set<Tuple> tuples = jedis.zrevrangeWithScores(Constants.SALES_LIST, 0, 4);
        List<PhoneVO> list = new ArrayList<>();
        for (Tuple tuple : tuples) {
            PhoneVO vo = new PhoneVO();
            // 取出对应 phoneId 的手机名称
            int phoneId = Integer.parseInt(tuple.getElement());
            vo.setName(phones.get(phoneId - 1).getName());
            vo.setSales((int) tuple.getScore());
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<DynamicVO> getBuyDynamic() {

        List<DynamicVO> dynamicList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            //获得销售动态，此处只取 3 条，同时队列只保存最新的 20 条动态
            String result = jedis.lindex(Constants.BUY_DYNAMIC, i);
            if (StringUtils.isEmpty(result)) {
                break;
            }
            String[] arr = result.split(Constants.separator);
            long time = Long.valueOf(arr[0]);
            String phone = arr[1];
            DynamicVO vo = new DynamicVO();
            vo.setPhone(phone);
            vo.setTime(showTime(new Date(time)));
            dynamicList.add(vo);
        }

        // 只保留队列中20个销售动态
        jedis.ltrim(Constants.BUY_DYNAMIC, 0, 19);
        return dynamicList;
    }

    @Override
    public int phoneRank(int phoneId) {
        // 如果是排名第一， 返回的是0 ，因此如果为null 即不在排行榜上则返回-1
        Long zrank = jedis.zrevrank(Constants.SALES_LIST, String.valueOf(phoneId));
        return zrank == null ? -1 : zrank.intValue();
    }

    @Override
    public void clear() {
        jedis.del(Constants.SALES_LIST);
        jedis.del(Constants.BUY_DYNAMIC);
    }

    @Override
    public void initCache() {
        Map<String, Double> map = new HashMap<>();
        map.put("1", 4.0);
        map.put("2", 2.0);
        map.put("3", 3.0);
        jedis.zadd(Constants.SALES_LIST, map);
    }


    public static String showTime(Date date) {
        if (date == null) {
            return null;
        } else {
            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            c.set(11, 0);
            c.set(12, 0);
            c.set(13, 0);
            SimpleDateFormat df;
            if (date.getTime() > c.getTimeInMillis()) {
                float diffSecond = (float) ((System.currentTimeMillis() - date.getTime()) / 1000L);
                if (diffSecond <= 3.0F) {
                    return "3秒前";
                }

                if (diffSecond <= 10.0F) {
                    return "10秒前";
                }

                if (diffSecond <= 50.0F) {
                    return (int) Math.ceil((double) (diffSecond / 10.0F)) * 10 + "秒前";
                }

                if (diffSecond < 3600.0F) {
                    return (int) Math.ceil((double) (diffSecond / 60.0F)) + "分钟前";
                }

                if (diffSecond < 86400.0F) {
                    df = new SimpleDateFormat("HH:mm");
                    return "今天" + df.format(date);
                }
            }

            c.add(5, -1);
            if (date.getTime() > c.getTimeInMillis()) {
                df = new SimpleDateFormat("HH:mm");
                return "昨天" + df.format(date);
            } else {
                if (c.get(1) == 1900 + date.getYear()) {
                    df = new SimpleDateFormat("MM-dd HH:mm");
                } else {
                    df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                }

                return df.format(date);
            }
        }
    }
}
