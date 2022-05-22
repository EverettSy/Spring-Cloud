package com.syrobin.cloud.model.param;

import lombok.Data;

/**
 * @author syrobin
 * @version v1.0
 * @description: 基础分页参数
 * @date 2022-04-22 14:57
 */
@Data
public class BasePageParam {
    /**
     * 当前页
     */
    private Integer pageNum = 1;
    /**
     * 每页数量
     */
    private Integer pageSize = 10;
    private String orderBy;
    private String orderSort;


}
