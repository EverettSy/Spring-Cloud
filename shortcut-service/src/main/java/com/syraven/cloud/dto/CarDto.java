package com.syraven.cloud.dto;

import lombok.Data;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-05-09 21:48
 */
@Data
public class CarDto {

    private String name;
    private String type;
    private Integer seats;
    private Integer seatCounts;
}
