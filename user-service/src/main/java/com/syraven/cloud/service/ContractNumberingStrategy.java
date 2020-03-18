package com.syraven.cloud.service;

/**
 * <<编号策略>>
 *
 * @author Raven
 * @date 2019/12/17 18:15
 */
public interface ContractNumberingStrategy {

    /**
     * 编号类型
     *
     * @return
     */
    String getType();


    /**
     * 获取合同编号
     * @param cityCode
     * @param company
     * @return
     */
    String getContractNumCode(String cityCode, String company);


}
