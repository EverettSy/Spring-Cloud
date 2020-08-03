package com.syraven.cloud.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <<功能简述>>
 *
 * @author Raven
 * @date 2020/6/30 17:17
 */
@Data
public class DownloadData {

    @ExcelProperty("字符串标题")
    private String name;
    @ExcelProperty("日期标题")
    private Date uploadDate;
    @ExcelProperty("数组标题")
    private BigDecimal doubleData;
}