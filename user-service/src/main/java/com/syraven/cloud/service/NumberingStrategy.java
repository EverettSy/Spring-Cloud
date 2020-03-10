package com.syraven.cloud.service;

/**
 * <<编号策略>>
 *
 * @author Raven
 * @date 2019/12/17 18:15
 */
public interface NumberingStrategy {

    /**
     * 编号类型
     *
     * @return
     */
    String getType();

    /**
     * 生成编号
     *
     * @param cityCode
     * @return
     */
    String getCode(String cityCode);
}
