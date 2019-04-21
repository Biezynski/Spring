package biezynski.bank.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import biezynski.bank.auth.Login;

@Component("loginValidator")
public class LoginValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Login.class.equals(clazz);
	}

	@Override
	public void validate(Object obj, Errors errors) {
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "username.required", "jdhasjdasd");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "asdasdasda");
		
		
	}
}