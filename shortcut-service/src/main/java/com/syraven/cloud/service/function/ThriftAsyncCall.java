package com.syraven.cloud.service.function;

import com.syraven.cloud.common.exception.ShortCutException;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-10-23 18:27
 */
@FunctionalInterface
public interface ThriftAsyncCall {

    void invoke() throws ShortCutException;
}
