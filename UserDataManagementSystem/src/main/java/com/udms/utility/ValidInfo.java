package com.udms.utility;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.udms.service.CommonValidationService;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = CommonValidationService.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInfo {
	 String message() default "Invalid map only age,occupation,contact,nid,likings are allowed";
	 Class<?>[] groups() default {};
	 Class<? extends Payload>[] payload() default {};
}