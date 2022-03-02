package com.syrobin.cloud.mybatis.injector.methods;

import com.syrobin.cloud.mybatis.injector.RavenSqlMethod;

/**
 * 插入一条数据（选择字段插入）插入如果中已经存在相同的记录，则忽略当前新数据
 *
 * @author L.cm
 * @since 2021-04-02 17:14
 */
public class InsertIgnore  extends AbstractInsertMethod{
    public InsertIgnore() {
        super(RavenSqlMethod.INSERT_IGNORE_ONE);
    }
}
