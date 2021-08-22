package com.syraven.cloud.controller;

import com.syraven.cloud.config.ServerInitConfiguration;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @ClassName IndexController
 * @Description: 页面控制器
 * @Author syrobin
 * @Date 2021-08-11 3:16 下午
 * @Version V1.0
 **/
@Controller
public class IndexController {

    @Resource
    private ServerInitConfiguration serverInitConfiguration;

    @Value("${common.domain}")
    private String domain;

    @GetMapping("/")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("domain", StringUtils.isEmpty(domain) ? serverInitConfiguration.getUrl():domain);
        return mv;
    }
}
