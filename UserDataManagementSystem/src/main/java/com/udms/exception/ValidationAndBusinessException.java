package com.udms.exception;

public class ValidationAndBusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private final String errorMessage;

    public ValidationAndBusinessException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
