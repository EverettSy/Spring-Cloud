package com.syraven.cloud.service.impl;

import com.syraven.cloud.constants.CityCodeConstants;
import com.syraven.cloud.service.NumberingStrategy;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * <<出租交割编号 >>
 * 生成规则 HZ 01 R0635O1STE（生成一个10位的随机数，大写字母+数字组合）
 *
 * @author Raven
 * @date 2019/12/20 16:15
 */
@Service
public class RentDeliveriesCodeServiceImpl implements NumberingStrategy {

    @Override
    public String getType() {
        return "CZJG";
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
        } /*else if ("029".equals(cityCode)) {
            //西安序号
            return getType() + CityCodeConstants.XA_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0551".equals(cityCode)) {
            //合肥序号
            return getType() + CityCodeConstants.HF_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0371".equals(cityCode)) {
            //郑州序号
            return getType() + CityCodeConstants.ZZ_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0574".equals(cityCode)) {
            //宁波序号
            return getType() + CityCodeConstants.NB_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("025".equals(cityCode)) {
            //南京序号
            return getType() + CityCodeConstants.NJ_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0769".equals(cityCode)) {
            //东莞序号
            return getType() + CityCodeConstants.DG_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0532".equals(cityCode)) {
            //青岛序号
            return getType() + CityCodeConstants.QD_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0871".equals(cityCode)) {
            //昆明序号
            return getType() + CityCodeConstants.KM_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("024".equals(cityCode)) {
            //沈阳序号
            return getType() + CityCodeConstants.SY_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0510".equals(cityCode)) {
            //无锡序号
            return getType() + CityCodeConstants.WX_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0757".equals(cityCode)) {
            //佛山序号
            return getType() + CityCodeConstants.FS_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0411".equals(cityCode)) {
            //大连序号
            return getType() + CityCodeConstants.DL_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0591".equals(cityCode)) {
            //福州序号
            return getType() + CityCodeConstants.FZ_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0592".equals(cityCode)) {
            //厦门序号
            return getType() + CityCodeConstants.XM_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0577".equals(cityCode)) {
            //温州序号
            return getType() + CityCodeConstants.WZ_CITY_CODE_PREFIX + generateGiftCode();
        } else if ("0451".equals(cityCode)) {
            //哈尔滨序号
            return getType() + CityCodeConstants.HRB_CITY_CODE_PREFIX + generateGiftCode();
        }*/
        return getType() + CityCodeConstants.HZ_CITY_CODE_PREFIX + generateGiftCode();
    }
}