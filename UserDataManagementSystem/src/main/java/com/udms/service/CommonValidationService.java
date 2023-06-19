package com.udms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.udms.utility.ValidInfo;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CommonValidationService implements ConstraintValidator<ValidInfo, Map<String, Object>> {
	public boolean isValid(Map<String, Object> map, ConstraintValidatorContext context) {
			List<String> errors = new ArrayList();
			for(String key : map.keySet()) {
				Object value = map.get(key);
				switch(key) {
				case "age" : if((value instanceof Integer && (Integer)value <18)||!(value instanceof Integer))
					errors.add("Invalid age");break;
				case "occupation" : if(!(value instanceof String))errors.add("Invalid occupation");break;
				case "contact" : if(!(value instanceof String))errors.add("Invalid contact");break;
				case "likings" : if(!(value instanceof List))errors.add("Invalid likings");break;
				default: break;
				}
			}
			if(!errors.isEmpty()) {
				String errorMessage = errors.parallelStream().collect(Collectors.joining(","));
				context.buildConstraintViolationWithTemplate(errorMessage)
				.addConstraintViolation().disableDefaultConstraintViolation();
				return false;
			}
			return true;
	}
}
