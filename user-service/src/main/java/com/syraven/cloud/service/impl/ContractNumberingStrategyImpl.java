package com.syraven.cloud.service.impl;

import com.syraven.cloud.constants.CityCodeConstants;
import com.syraven.cloud.service.ContractNumberingStrategy;
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
 * @date 2020/3/13 17:42
 */
@Service
public class ContractNumberingStrategyImpl implements ContractNumberingStrategy {

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


    /**
     * 编号类型
     *
     * @return
     */
    @Override
    public String getType() {
        return "HZCK-CZ";
    }

    /**
     * 获取合同编号
     *
     * @param cityCode
     * @param company
     * @return
     */
    @Override
    public String getContractNumCode(String cityCode, String company) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String formatDate = sdf.format(date);
        String key = getType() + company + formatDate;
        Long incr = getIncr(key, getCurrent2TodayEndMillisTime());
        if (incr == 0) {
            //
            incr = getIncr(key, getCurrent2TodayEndMillisTime());
        }
        DecimalFormat df = new DecimalFormat("0000");
        if ("0571".equals(cityCode)) {
            //杭州序号
            return company + "-" + CityCodeConstants.HZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0512".equals(cityCode)) {
            //苏州序号
            return company + "-" + CityCodeConstants.SZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("027".equals(cityCode)) {
            //武汉序号
            return company + "-" + CityCodeConstants.WH_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("021".equals(cityCode)) {
            //上海序号
            return company + "-" + CityCodeConstants.SH_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("028".equals(cityCode)) {
            //成都序号
            return company + "-" + CityCodeConstants.CD_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("022".equals(cityCode)) {
            //成都序号
            return company + "-" + CityCodeConstants.TJ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0755".equals(cityCode)) {
            //深圳序号
            return company + "-" + CityCodeConstants.SZS_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0731".equals(cityCode)) {
            //长沙序号
            return company + "-" + CityCodeConstants.CS_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("010".equals(cityCode)) {
            //北京序号
            return company + "-" + CityCodeConstants.BJ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("020".equals(cityCode)) {
            //广州序号
            return company + "-" + CityCodeConstants.GZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("023".equals(cityCode)) {
            //重庆序号
            return company + "-" + CityCodeConstants.CQ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("029".equals(cityCode)) {
            //西安序号
            return company + "-" + CityCodeConstants.XA_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0551".equals(cityCode)) {
            //合肥序号
            return company + "-" + CityCodeConstants.HF_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0371".equals(cityCode)) {
            //郑州序号
            return company + "-" + CityCodeConstants.ZZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0574".equals(cityCode)) {
            //宁波序号
            return company + "-" + CityCodeConstants.NB_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("025".equals(cityCode)) {
            //南京序号
            return company + "-" + CityCodeConstants.NJ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0769".equals(cityCode)) {
            //东莞序号
            return company + "-" + CityCodeConstants.DG_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0532".equals(cityCode)) {
            //青岛序号
            return company + "-" + CityCodeConstants.QD_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0871".equals(cityCode)) {
            //昆明序号
            return company + "-" + CityCodeConstants.KM_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("024".equals(cityCode)) {
            //沈阳序号
            return company + "-" + CityCodeConstants.SY_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0510".equals(cityCode)) {
            //无锡序号
            return company + "-" + CityCodeConstants.WX_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0757".equals(cityCode)) {
            //佛山序号
            return company + "-" + CityCodeConstants.FS_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0411".equals(cityCode)) {
            //大连序号
            return company + "-" + CityCodeConstants.DL_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0591".equals(cityCode)) {
            //福州序号
            return company + "-" + CityCodeConstants.FZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0592".equals(cityCode)) {
            //厦门序号
            return company + "-" + CityCodeConstants.XM_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0577".equals(cityCode)) {
            //温州序号
            return company + "-" + CityCodeConstants.WZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
        } else if ("0451".equals(cityCode)) {
            //哈尔滨序号
            return company + "-" + CityCodeConstants.HRB_CITY_CODE_PREFIX + formatDate + df.format(incr);
        }
        return company + "-" + CityCodeConstants.HZ_CITY_CODE_PREFIX + formatDate + df.format(incr);
    }
}