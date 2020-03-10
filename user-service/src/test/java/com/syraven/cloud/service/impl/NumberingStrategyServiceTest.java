package com.syraven.cloud.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NumberingStrategyServiceTest {

    @Autowired
    private NumberingStrategyService numberingStrategyService;

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
        String re = "黑龙江省";
        int se = re.lastIndexOf("省");
        System.out.println(se);
        String res = re.substring(0,re.indexOf("省"))+"省";
        System.out.println(res);

       /* String czDCode = numberingStrategyService.getCode("CZJG","023");
        System.out.println(czDCode);
*/
        String czDCode = numberingStrategyService.getCode("HS","0574");
        System.out.println(czDCode);
    }




}