package com.syraven.cloud.log.action;

import java.util.Arrays;
import java.util.function.BiFunction;

/**
 * @author syrobin
 * @version v1.0
 * @description: 异常情况返回默认值
 * 当发生异常时返回自定义的值
 * @date 2022-10-23 19:09
 */
public class DefaultValueHandle<R> extends AbstractLogAction<R> implements BiFunction<R, Throwable, R> {

    private final R defaultValue;

    /**
     * 当返回值为空的时候是否替换为默认值
     */
    private final boolean isNullToDefault;

    /**
     * @param methodName   方法名
     * @param defaultValue 当异常发生时自定义返回的默认值
     * @param args         方法参数
     */
    public DefaultValueHandle(String methodName, R defaultValue, Object... args) {
        super(methodName, args);
        this.defaultValue = defaultValue;
        this.isNullToDefault = false;
    }

    /**
     * @param isNullToDefault
     * @param defaultValue    当异常发生时自定义返回的默认值
     * @param methodName      方法名称
     * @param args            方法入参
     */
    public DefaultValueHandle(boolean isNullToDefault, R defaultValue, String methodName, Object... args) {
        super(methodName, args);
        this.defaultValue = defaultValue;
        this.isNullToDefault = isNullToDefault;
    }

    @Override
    public R apply(R result, Throwable throwable) {
        logResult(result, throwable);
        if (throwable != null) {
            return defaultValue;
        }
        if (result == null && isNullToDefault) {
            return defaultValue;
        }
        return result;
    }

    public static <R> DefaultValueHandle.DefaultValueHandleBuilder<R> builder() {
        return new DefaultValueHandle.DefaultValueHandleBuilder<>();
    }

    private static class DefaultValueHandleBuilder<R> {
        private boolean isNullToDefault;
        private R defaultValue;
        private String methodName;
        private Object[] args;

        DefaultValueHandleBuilder() {
        }

        public DefaultValueHandle.DefaultValueHandleBuilder<R> isNullToDefault(final boolean isNullToDefault) {
            this.isNullToDefault = isNullToDefault;
            return this;
        }

        public DefaultValueHandle.DefaultValueHandleBuilder<R> defaultValue(final R defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public DefaultValueHandle.DefaultValueHandleBuilder<R> methodName(final String methodName) {
            this.methodName = methodName;
            return this;
        }

        public DefaultValueHandle.DefaultValueHandleBuilder<R> args(final Object... args) {
            this.args = args;
            return this;
        }

        public DefaultValueHandle<R> build() {
            return new DefaultValueHandle<R>(this.isNullToDefault, this.defaultValue, this.methodName, this.args);
        }

        public String toString() {
            return "DefaultValueHandle.DefaultValueHandleBuilder(isNullToDefault=" + this.isNullToDefault + ", defaultValue=" + this.defaultValue + ", methodName=" + this.methodName + ", args=" + Arrays.deepToString(this.args) + ")";
        }
    }
}
