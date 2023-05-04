package cobook.buddywisdom.global.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class YearMonthValidator implements ConstraintValidator<YearMonth, String> {
	@Override
	public void initialize(YearMonth constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		return value.matches("^\\d{4}-\\d{2}$");
	}
}
