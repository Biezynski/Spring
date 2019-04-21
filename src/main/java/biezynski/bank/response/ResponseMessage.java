package biezynski.bank.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseMessage {

	SUCCESS("INF001", "Operacja zakonczona sukcesem"),
	ACCOUNT_NOT_FOUND("ERR001", "Konto nie znalezione"), 
	ACCOUNT_ALREADY_EXISTS("ERR002", "Konto juz istnieje"), 
	INVALID_ID("ERR003", "Wprowadzone nieprawidlowe dane"),
	INVALID_AMMOUNT("ERR004", "Niewlasciwa ilosc"), 
	LOAN_CREDIT_EXCEEDED("ERR005", "Wymagana kwota kredytu przekracza dostepny limit kredytowy"), 
	LOAN_NOT_ENOUGH_BALANCE("ERR006", "Niewystarczajace saldo na rachunku dla operacji kredytowej. Wymagane minimum 1000"),
	LOAN_PENDING_FOR_ACCOUNT("ERR007", "Na to konto czeka oczekujaca pozyczka"), 
	OPERATION_ERROR("ERR999", "Nieznany blad operacji");

	private ResponseMessage(String code, String message) {
		this.code = code;
		this.message = message;
	}

	final private String code;
	final private String message;

	public String getCode() {
		return code;
	}

	@JsonValue
	public String getMessage() {
		return message;
	}
}