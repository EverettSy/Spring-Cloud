package com.syraven.cloud.controller;

import com.syraven.cloud.spring.context.bean.ChildBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TestController
 * @Description:
 * @Author syrobin
 * @Date 2021-10-18 2:09 下午
 * @Version V1.0
 */
@RestController
public class TestController {

    @Autowired
    private ChildBean childBean;

    @RequestMapping("/test")
    public ChildBean getChildBean() {
        return childBean;
    }

}
