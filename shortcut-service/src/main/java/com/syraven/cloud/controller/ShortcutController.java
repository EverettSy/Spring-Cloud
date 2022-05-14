package com.syraven.cloud.controller;

import com.syraven.cloud.config.ServerInitConfiguration;
import com.syraven.cloud.service.UrlConvertService;
import com.syraven.cloud.record.CommonResult;
import com.syraven.cloud.utlis.QRcodeUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

/**
 * @ClassName ShortcutController
 * @Description: 短链服务API
 * @Author syrobin
 * @Date 2021-08-11 3:32 下午
 * @Version V1.0
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/short")
public class ShortcutController {

    private final UrlConvertService urlConvertService;

    private final ServerInitConfiguration serverInitConfiguration;

    @Value("${common.domain}")
    private String domain;

    @ApiOperation(value = "转为短链")
    @PostMapping("/convert")
    public CommonResult<String> convertUrl(@RequestParam String url){
        String shortUrl = urlConvertService.convertUrl(url);
        String coverUrl = StringUtils.isEmpty(domain) ? serverInitConfiguration.getUrl()+"/"+shortUrl:domain+"/"+shortUrl;
        return CommonResult.success(coverUrl);
    }


    @ApiOperation(value = "短链转为标准链接")
    @PostMapping("/revert")
    public CommonResult<String> revertUrl(@RequestParam String shortUrl) {
        return CommonResult.success(urlConvertService.revertUrl(shortUrl));

    }

    @SneakyThrows
    @ApiOperation(value = "生成二维码",notes = "将链接生成二维码")
    @GetMapping(value = "/qrcode",produces = MediaType.IMAGE_JPEG_VALUE)
    public BufferedImage getImage(@RequestParam String url){
        return QRcodeUtils.QREncode(url);
    }
}
