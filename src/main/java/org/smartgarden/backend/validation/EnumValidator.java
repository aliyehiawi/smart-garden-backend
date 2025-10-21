package org.smartgarden.backend.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
    
    private Class<? extends Enum<?>> enumClass;
    private boolean ignoreCase;
    
    @Override
    public void initialize(ValidEnum annotation) {
        this.enumClass = annotation.enumClass();
        this.ignoreCase = annotation.ignoreCase();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; 
        }
        
        Enum<?>[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants == null) {
            return false;
        }
        
        for (Enum<?> enumConstant : enumConstants) {
            if (ignoreCase) {
                if (enumConstant.name().equalsIgnoreCase(value)) {
                    return true;
                }
            } else {
                if (enumConstant.name().equals(value)) {
                    return true;
                }
            }
        }
        
        String validValues = Arrays.stream(enumConstants)
                .map(Enum::name)
                .collect(Collectors.joining(", "));
        
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                "Invalid value '" + value + "'. Must be one of: " + validValues
        ).addConstraintViolation();
        
        return false;
    }
}

