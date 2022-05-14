package com.syraven.cloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-04-08 10:16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutMessage {

    private String from;
    private String content;
    private Date time;

    public OutMessage(String content) {
        this.content = content;
    }
}
