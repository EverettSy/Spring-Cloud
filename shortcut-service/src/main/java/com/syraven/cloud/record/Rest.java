package com.syraven.cloud.record;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-04-15 17:11
 */
public interface Rest<T> {
    /**
     * 状态码 .
     *
     * @param httpStatus the http status
     */
    void setHttpStatus(int httpStatus);

    /**
     * 数据载体.
     *
     * @param data the data
     */
    void setData(T data);

    /**
     * 提示信息.
     *
     * @param msg the msg
     */
    void setMsg(String msg);

    /**
     * Sets identifier.
     *
     * @param identifier 标识
     */
    void setIdentifier(String identifier);
}
