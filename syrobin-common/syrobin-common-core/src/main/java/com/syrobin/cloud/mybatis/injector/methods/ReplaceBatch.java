package com.syrobin.cloud.mybatis.injector.methods;


import com.syrobin.cloud.mybatis.injector.RavenSqlMethod;

/**
 * 插入一条数据（选择字段插入）
 * <p>
 * 表示插入替换数据，需求表中有PrimaryKey，或者unique索引，如果数据库已经存在数据，则用新数据替换，如果没有数据效果则和insert into一样；
 * </p>
 *
 * @author L.cm
 */
public class ReplaceBatch extends AbstractInsertBatch {
    private static final String SQL_METHOD = "replaceBatch";
    public ReplaceBatch() {
        super(RavenSqlMethod.REPLACE_ONE.getSql(), SQL_METHOD);
    }
}
