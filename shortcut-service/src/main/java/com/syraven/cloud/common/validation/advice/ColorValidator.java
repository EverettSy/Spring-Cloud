package com.syraven.cloud.common.validation.advice;

import com.syraven.cloud.common.enums.Colors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author syrobin
 * @version v1.0
 * @description:
 * @date 2022-04-15 17:02
 */
public class ColorValidator implements ConstraintValidator<Color,String> {

    private static final Set<String> COLOR_CONSTRAINTS = new HashSet<>();

    @Override
    public void initialize(Color constraintAnnotation) {
        Colors[] colors = constraintAnnotation.value();
        List<String> list = Arrays.stream(colors)
                .map(Enum::name)
                .collect(Collectors.toList());
        COLOR_CONSTRAINTS.addAll(list);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return COLOR_CONSTRAINTS.contains(value);
    }
}
