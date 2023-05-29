package com.michaeltang.usermanagement.common.exception;

public class ValidationException extends Exception {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7318016056112220443L;

	public ValidationException(String message) {
        super("Validation failed: " + message);
    }
}