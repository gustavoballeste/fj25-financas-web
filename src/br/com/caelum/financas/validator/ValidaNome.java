package br.com.caelum.financas.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

@NotNull
@Length(min=5)
@Pattern(regexp="[A-Z].*")
@Constraint(validatedBy={})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidaNome {
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
	String message() default "{minha.msg}";
}
