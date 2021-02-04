package com.syraven.cloud.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/11/11 9:48
 */
@Data
@ToString
@NoArgsConstructor                 //无参构造
@AllArgsConstructor
public class Book {

    private String name;
    private Integer releaseYear;
    private String isbn;

}