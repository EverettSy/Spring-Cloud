package com.syraven.cloud.common.validation.advice;

import com.syraven.cloud.common.enums.Colors;

import javax.validation.Constraint;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-04-15 16:52
 */
@Constraint(validatedBy = ColorValidator.class)
@Documented
@Target({ElementType.METHOD,ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR,
    ElementType.PARAMETER,ElementType.TYPE_USE})
public @interface Color {

    String message() default "颜色不符合规格";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    Colors[] value();
}

