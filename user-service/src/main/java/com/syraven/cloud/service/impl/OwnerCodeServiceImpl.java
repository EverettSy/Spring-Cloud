package com.syraven.cloud.service.impl;

import com.syraven.cloud.constants.CityCodeConstants;
import com.syraven.cloud.service.NumberingStrategy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2019/12/20 11:11
 */
@Service
public class OwnerCodeServiceImpl implements NumberingStrategy {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    /**
     * 生成序列号规则
     *
     * @param key
     * @param liveTime 设置过期时间
     * @return
     */
    private Long getIncr(String key, long liveTime) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();

        //初始设置过期时间
        if ((null == increment || increment.longValue() == 0) && liveTime > 0) {
            //单位毫秒
            entityIdCounter.expire(liveTime, TimeUnit.MILLISECONDS);
        }
        return increment;
    }

    /**
     * 现在到今天结束的毫秒数
     *
     * @return
     */
    private Long getCurrent2TodayEndMillisTime() {
        Calendar todayEnd = Calendar.getInstance();
        // Calendar.HOUR 12小时制
        // HOUR_OF_DAY 24小时制
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTimeInMillis() - System.currentTimeMillis();
    }


    @Override
    public String getType() {
        return "FD";
    }

    @Override
    public String getCode(String cityCode) {
        //日期格式化
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String formatDate = sdf.format(date);
        String key = getType() + formatDate;
        Long incr = getIncr(key,getCurrent2TodayEndMillisTime());
        if (incr == 0){
            //
            incr = getIncr(key,getCurrent2TodayEndMillisTime());
        }
        DecimalFormat df = new DecimalFormat("0000");
        if ("0571".equals(cityCode)){
            //杭州序号
            return getType() + CityCodeConstants.HZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }else if ("0512".equals(cityCode)){
            //苏州序号
            return getType() + CityCodeConstants.SZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }else if ("027".equals(cityCode)){
            //武汉序号
            return getType() + CityCodeConstants.WH_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }else if ("021".equals(cityCode)){
            //上海序号
            return getType() + CityCodeConstants.SH_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }else if ("028".equals(cityCode)){
            //成都序号
            return getType() + CityCodeConstants.CD_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }else if ("022".equals(cityCode)){
            //成都序号
            return getType() + CityCodeConstants.TJ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }else if ("0755".equals(cityCode)){
            //深圳序号
            return getType() + CityCodeConstants.SZS_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }else if ("0731".equals(cityCode)){
            //长沙序号
            return getType() + CityCodeConstants.CS_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }else if ("010".equals(cityCode)){
            //北京序号
            return getType() + CityCodeConstants.BJ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }else if ("020".equals(cityCode)){
            //广州序号
            return getType() + CityCodeConstants.GZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }else if ("023".equals(cityCode)){
            //重庆序号
            return getType() + CityCodeConstants.CQ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }
        return getType() + CityCodeConstants.HZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
    }
}