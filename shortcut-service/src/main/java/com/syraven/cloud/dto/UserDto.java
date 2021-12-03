package com.syraven.cloud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @ClassName: UserDto
 * @Description: 用户DTO
 * @Author syrobin
 * @Date 2021-11-23 10:42 上午
 * @Version V1.0
 */
@Data
@ToString
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    private LocalDateTime registerTime;
}
