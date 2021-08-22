package com.syraven.cloud.controller;

import com.syraven.cloud.service.UrlConvertService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName RedirectController
 * @Description: 重定向控制器
 * @Author syrobin
 * @Date 2021-08-21 10:07 下午
 * @Version V1.0
 **/
@Controller
public class RedirectController {

    @Resource
    private UrlConvertService urlConvertService;


    /**
     * 302 重定向到新的地址
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/*")
    public RedirectView redirect(HttpServletRequest request, HttpServletResponse response) {
        String shortcut = request.getServletPath().substring(1);
        String url = urlConvertService.revertUrl(shortcut);
        return new RedirectView(url);
    }
}
