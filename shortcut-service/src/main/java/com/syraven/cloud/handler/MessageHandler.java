package com.syraven.cloud.handler;

import com.syraven.cloud.domain.TaskInfo;

/**
 * @author syrobin
 * @version v1.0
 * @description: 消息处理器
 * @date 2022-04-11 10:15
 */
public interface MessageHandler {

    /**
     * 处理器
     * @param taskInfo 任务信息
     */
    void doHandler(TaskInfo taskInfo);
}
