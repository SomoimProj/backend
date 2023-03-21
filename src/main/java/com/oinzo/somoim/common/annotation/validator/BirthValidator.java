package com.oinzo.somoim.common.annotation.validator;

import com.oinzo.somoim.common.annotation.Birth;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BirthValidator implements ConstraintValidator<Birth, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}

		// 8자리 숫자
		return value.matches("(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])");
	}

}
