package com.syraven.cloud.core.mybatis.injector.methods;

import com.baomidou.mybatisplus.core.enums.SqlMethod;

/**
 * 批量插入数据
 *
 * @author L.cm
 * @since 2021-04-02 17:07
 */
public class InsertBatch extends AbstractInsertBatch {
    public static final String SQL_METHOD = "insertBatch";
    public InsertBatch() {
        super(SqlMethod.INSERT_ONE.getSql(), SQL_METHOD);
    }
}
