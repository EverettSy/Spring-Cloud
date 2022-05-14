package com.syraven.cloud.common.exception;

/**
 * 项目异常
 */
public class ProjectException extends RuntimeException {

    public ProjectException(String message) {
        super(message);
    }

    public ProjectException(String message, Throwable cause) {
        super(message, cause);
    }

}
