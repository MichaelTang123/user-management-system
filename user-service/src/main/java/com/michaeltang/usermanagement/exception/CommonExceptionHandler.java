package com.michaeltang.usermanagement.exception;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.michaeltang.usermanagement.controller.UserController;
import com.michaeltang.usermanagement.common.spi.ApiError;
import com.michaeltang.usermanagement.common.exception.ValidationException;


/**
 * Common error message converter for HTTP response.
 * @author tangyh
 *
 */

@RestControllerAdvice(assignableTypes = UserController.class)
@ResponseBody
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonExceptionHandler {
	@Value(value = "${api.exception.message: error}")
    private String message;
    
    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> methodArgumentNotValidException(MethodArgumentNotValidException exception)
    {
		return ResponseEntity.badRequest().body(ApiError.from(
        		HttpStatus.BAD_REQUEST,
        		"Validation error",
        		exception.getMessage()));
    }
	
	@ExceptionHandler(value = { ValidationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> validationException(ValidationException exception)
    {
		return ResponseEntity.badRequest().body(ApiError.from(
        		HttpStatus.BAD_REQUEST,
        		"Validation error",
        		exception.getMessage()));
    }
	
	@ExceptionHandler(value = { DataIntegrityViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> dataIntegrityViolationException(DataIntegrityViolationException exception)
    {
		return ResponseEntity.badRequest().body(ApiError.from(
        		HttpStatus.BAD_REQUEST,
        		"DB error",
        		exception.getMessage()));
    }
	
	@ExceptionHandler(value = { SQLException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> sqlException(SQLException exception)
    {
		return ResponseEntity.badRequest().body(ApiError.from(
        		HttpStatus.BAD_REQUEST,
        		"DB error",
        		exception.getMessage()));
    }
    
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiError> commonException(Exception exception) {
    	return ResponseEntity.internalServerError().body(ApiError.from(
        		HttpStatus.BAD_REQUEST,
        		message,
        		exception.getMessage()));
    }
}
