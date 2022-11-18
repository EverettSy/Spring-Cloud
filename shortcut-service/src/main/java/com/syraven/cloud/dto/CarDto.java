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

    public CarDto(String name, String type, Integer seats, Integer seatCounts) {
        this.name = name;
        this.type = type;
        this.seats = seats;
        this.seatCounts = seatCounts;
    }

    public CarDto(String name, String type, Integer seats) {
        this.name = name;
        this.type = type;
        this.seats = seats;
    }

    public CarDto(CarDto carDto) {
        this.name = name;
        this.type = type;
        this.seats = seats;
    }

}
