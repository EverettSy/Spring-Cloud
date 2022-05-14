package com.syraven.cloud.handler;

import com.syraven.cloud.common.enums.AnchorState;
import com.syraven.cloud.domain.AnchorInfo;
import com.syraven.cloud.domain.TaskInfo;
import com.syraven.cloud.utlis.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

/**
 * @author syrobin
 * @version v1.0
 * @description: 发送各个渠道的handler
 * @date 2022-04-11 10:41
 */
public abstract class BaseHandler implements MessageHandler {

    /**
     * 标识渠道的Code
     * 子类初始化的时候指定
     */
    protected Integer channelCode;


    @Autowired
    private HandlerHolder handlerHolder;
    @Autowired
    private LogUtils logUtils;

    @PostConstruct
    private void init() {
        handlerHolder.putHandler(channelCode, this);
    }


    @Override
    public void doHandler(TaskInfo taskInfo) {
        if (handler(taskInfo)) {
            logUtils.print(AnchorInfo.builder()
                    .state(AnchorState.SEND_SUCCESS.getCode())
                    .businessId(taskInfo.getBusinessId())
                    .ids(taskInfo.getReceiver())
                    .build());
            return;
        }
        logUtils.print(AnchorInfo.builder()
                .state(AnchorState.SEND_FAIL.getCode())
                .businessId(taskInfo.getBusinessId())
                .ids(taskInfo.getReceiver())
                .build());
    }


    /**
     * 统一处理的handler接口
     *
     * @param taskInfo
     * @return
     */
    public abstract boolean handler(TaskInfo taskInfo);
}
