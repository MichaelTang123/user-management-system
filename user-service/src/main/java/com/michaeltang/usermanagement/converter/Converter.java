package com.michaeltang.usermanagement.converter;

import com.michaeltang.usermanagement.common.exception.ValidationException;

public interface Converter {
	Object convert(Object entity) throws ValidationException;
}
