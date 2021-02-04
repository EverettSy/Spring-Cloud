package com.syraven.cloud.service.captcha;

import com.syraven.cloud.CaptchaType;

import java.io.File;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/10/23 10:24
 */
public abstract class AbstractCaptchaRecognition {

    public abstract String dentificationCodesi(File file, CaptchaType captchaType);
}