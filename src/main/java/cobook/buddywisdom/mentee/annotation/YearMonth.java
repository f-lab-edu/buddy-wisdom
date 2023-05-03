package cobook.buddywisdom.mentee.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = YearMonthValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface YearMonth {

	String message() default "날짜 형식이 맞지 않습니다.";

	Class<?> [] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
