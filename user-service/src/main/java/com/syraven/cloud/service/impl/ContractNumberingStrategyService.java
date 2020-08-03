package com.syraven.cloud.service.impl;

import com.syraven.cloud.service.ContractNumberingStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2019/12/20 15:21
 */
@Service("SD")
public class ContractNumberingStrategyService {

    Map<String, ContractNumberingStrategy> numberingStrategyMap = new HashMap<>();
    // 构造函数，如果你是集合接口对象，那么久会把spring容器中所有关于该接口的子类，全部抓出来放入到集合中
    public ContractNumberingStrategyService(List<ContractNumberingStrategy> numberingStrategyList){
       /* for (NumberingStrategy numberingStrategy : numberingStrategyList) {
            numberingStrategyMap.put(numberingStrategy.getType(),numberingStrategy);
        }*/
        numberingStrategyList.forEach(n -> numberingStrategyMap.put(n.getType(),n));
    }

    public String getContractNumCode(String type,String cityCode,String company){
        ContractNumberingStrategy numberingStrategy = (ContractNumberingStrategy) numberingStrategyMap.get(type);
        return numberingStrategy.getContractNumCode(cityCode,company);
    }
}