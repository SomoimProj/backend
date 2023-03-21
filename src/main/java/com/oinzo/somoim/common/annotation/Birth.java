package com.oinzo.somoim.common.annotation;

import com.oinzo.somoim.common.annotation.validator.BirthValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BirthValidator.class)
public @interface Birth {
	String message() default "생년월일을 8자리 숫자로 입력해주세요.";
	Class[] groups() default {};
	Class[] payload() default {};
}
