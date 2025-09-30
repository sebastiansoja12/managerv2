package com.warehouse.dangerousgood.domain.validator;

import com.warehouse.dangerousgood.domain.enumeration.ClassificationCode;
import org.springframework.stereotype.Component;

@Component
public class ClassificationCodeValidator {

    public static void validateClassificationCode(final String classificationCode) {
        try {
            ClassificationCode.valueOf(classificationCode);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid classification code");
        }
    }
}
