package com.syraven.cloud.service.impl;

import com.syraven.cloud.constants.CityCodeConstants;
import com.syraven.cloud.service.NumberingStrategy;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * <<房源编号 >>
 * 生成规则 HZ 01 R0635O1STE（生成一个10位的随机数，大写字母+数字组合）
 *
 * @author Raven
 * @date 2019/12/20 16:15
 */
@Service
public class EntrustChannelCodeServiceImpl implements NumberingStrategy {

    @Override
    public String getType() {
        return "WTQD";
    }

    //生成一个10位的随机数，大写字母+数字组合
    private static String generateGiftCode() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 65;
                char val = (char) (choice + random.nextInt(26));
                sb.append(val);
            } else {
                int num = random.nextInt(9);
                sb.append(num);
            }
        }
        return sb.toString();
    }

    @Override
    public String getCode(String cityCode) {
        if ("0571".equals(cityCode)) {
            //杭州序号
            return getType() + CityCodeConstants.HZ_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0512".equals(cityCode)) {
            //苏州序号
            return getType() + CityCodeConstants.SZ_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("027".equals(cityCode)) {
            //武汉序号
            return getType() + CityCodeConstants.WH_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("021".equals(cityCode)) {
            //上海序号
            return getType() + CityCodeConstants.SH_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("028".equals(cityCode)) {
            //成都序号
            return getType() + CityCodeConstants.CD_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("022".equals(cityCode)) {
            //成都序号
            return getType() + CityCodeConstants.TJ_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0755".equals(cityCode)) {
            //深圳序号
            return getType() + CityCodeConstants.SZS_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0731".equals(cityCode)) {
            //长沙序号
            return getType() + CityCodeConstants.CS_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("010".equals(cityCode)) {
            //北京序号
            return getType() + CityCodeConstants.BJ_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("020".equals(cityCode)) {
            //广州序号
            return getType() + CityCodeConstants.GZ_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("023".equals(cityCode)) {
            //重庆序号
            return getType() + CityCodeConstants.CQ_CITY_CODE_PREFIX + generateGiftCode();
        }
        return getType() + CityCodeConstants.HZ_CITY_CODE_PREFIX + generateGiftCode();
    }
}