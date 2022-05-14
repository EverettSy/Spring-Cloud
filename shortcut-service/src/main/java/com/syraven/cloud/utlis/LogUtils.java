package com.syraven.cloud.utlis;

import cn.monitor4all.logRecord.bean.LogDTO;
import cn.monitor4all.logRecord.service.IOperationLogGetService;
import com.alibaba.fastjson.JSON;
import com.syraven.cloud.domain.AnchorInfo;
import com.syraven.cloud.domain.LogParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author syrobin
 * @version v1.0
 * @description: 日志工具类
 * @date 2022-04-11 10:57
 */
@Slf4j
@Component
public class LogUtils implements IOperationLogGetService {


    /**
     * 方法切面的日志 @OperationLog 所产生的日志
     */
    @Override
    public void createLog(LogDTO logDTO) {
        log.info(JSON.toJSONString(logDTO));
    }

    /**
     * 记录当前对象信息
     */
    public void print(LogParam logParam) {
        logParam.setTimestamp(System.currentTimeMillis());
        log.info(JSON.toJSONString(logParam));
    }

    /**
     * 记录打点信息
     */
    public void print(AnchorInfo anchorInfo) {
        anchorInfo.setTimestamp(System.currentTimeMillis());
        String message = JSON.toJSONString(anchorInfo);
        log.info(message);

        //kafka 日志处理
        //try {
        //    kafkaUtils.send(topicName, message);
        //} catch (Exception e) {
        //    log.error("LogUtils#print kafka fail! e:{},params:{}", Throwables.getStackTraceAsString(e)
        //            , JSON.toJSONString(anchorInfo));
        //}
    }

    /**
     * 记录当前对象信息和打点信息
     */
    public void print(LogParam logParam, AnchorInfo anchorInfo) {
        print(anchorInfo);
        print(logParam);
    }
}
