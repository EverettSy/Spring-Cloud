package com.syraven.cloud.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author syrobin
 * @version v1.0
 * @description: channel->Handler的映射关系
 * @date 2022-04-11 10:35
 */
@Component
public class HandlerHolder {
    private final Map<Integer, MessageHandler> handlers = new HashMap<>(128);

    public void putHandler(Integer channelCode, MessageHandler handler) {
        handlers.put(channelCode, handler);
    }

    public MessageHandler route(Integer channelCode) {
        return handlers.get(channelCode);
    }
}
