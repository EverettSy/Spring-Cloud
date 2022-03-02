package com.syraven.cloud.listener;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.CustomLogListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName: LogListener
 * @Description: 日志本地监听
 * @Author syrobin
 * @Date 2022-01-27 2:26 PM
 * @Version V1.0
 */
@Slf4j
@Component
public class LogListener extends CustomLogListener {
    @Override
    public void createLog(LogDTO logDTO) throws Exception {
        log.info("LogListener 本地接收到日志 [{}]", logDTO);
    }
}
