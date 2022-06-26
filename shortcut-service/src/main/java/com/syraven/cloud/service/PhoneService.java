package com.syraven.cloud.service;

import com.syraven.cloud.vo.DynamicVO;
import com.syraven.cloud.vo.PhoneVO;

import java.util.List;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-06-25 17:11
 */
public interface PhoneService {

    /**
     * 购买
     * @param phoneId 手机ID
     */
    void buyPhone(int phoneId);

    /**
     * 获取销量排行榜
     * @return
     */
    List<PhoneVO> getPhbList();

    /**
     * 获得销售动态
     * @return
     */
    List<DynamicVO> getBuyDynamic();

    /**
     * 获得销售排行榜上该手机的排名
     * @param phoneId
     * @return
     */
    int phoneRank(int phoneId);

    /**
     * 清空缓存
     */
    void clear();

    /**
     * 排行榜丢失时，根据数据库数据来初始化排行榜
     */
    void initCache();
}
