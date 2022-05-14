package com.syraven.cloud.domain;

import lombok.Data;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-05-09 21:39
 */
@Data
public class Car {
    private String name;
    private String type;
    private Integer seatConfiguration;
    private Integer seatCount;
}

