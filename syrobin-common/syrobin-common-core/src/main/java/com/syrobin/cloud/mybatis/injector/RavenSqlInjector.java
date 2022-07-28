package com.syrobin.cloud.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.syrobin.cloud.mybatis.injector.methods.InsertBatch;
import com.syrobin.cloud.mybatis.injector.methods.InsertIgnore;
import com.syrobin.cloud.mybatis.injector.methods.InsertIgnoreBatch;
import com.syrobin.cloud.mybatis.injector.methods.Replace;
import com.syrobin.cloud.mybatis.injector.methods.ReplaceBatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义sql注入
 *
 * @author L.cm
 * @since 2021-04-01 15:08
 */
public class RavenSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = new ArrayList<>();
        methodList.add(new InsertBatch());
        methodList.add(new InsertIgnore());
        methodList.add(new InsertIgnoreBatch());
        methodList.add(new Replace());
        methodList.add(new ReplaceBatch());
        methodList.addAll(super.getMethodList(mapperClass, tableInfo));
        return Collections.unmodifiableList(methodList);
    }
}
