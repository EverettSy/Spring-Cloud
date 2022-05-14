package com.syraven.cloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author syrobin
 * @version v1.0
 * @description: 接受客户端数据实体
 * @date 2022-04-08 10:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class InMessage {
    private String from;
    private String to;
    private String content;
    private Date time;

    public InMessage(String content) {
        this.content = content;
    }
}
