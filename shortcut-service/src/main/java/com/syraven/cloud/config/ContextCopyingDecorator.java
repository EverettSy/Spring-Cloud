package com.syraven.cloud.config;

import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-03-23 23:52
 */
public class ContextCopyingDecorator implements TaskDecorator {
    @NotNull
    @Override
    public Runnable decorate(@NotNull Runnable runnable) {
        try {
            RequestAttributes context = RequestContextHolder.currentRequestAttributes();  //1
            Map<String,String> previous = MDC.getCopyOfContextMap(); 					  //2
            SecurityContext securityContext = SecurityContextHolder.getContext();	      //3
            return () -> {
                try {
                    RequestContextHolder.setRequestAttributes(context);	 //1
                    MDC.setContextMap(previous);					     //2
                    SecurityContextHolder.setContext(securityContext);   //3
                    runnable.run();
                } finally {
                    RequestContextHolder.resetRequestAttributes();		// 1
                    MDC.clear(); 										// 2
                    SecurityContextHolder.clearContext();				// 3
                }
            };
        } catch (IllegalStateException e) {
            return runnable;
        }
    }
}
