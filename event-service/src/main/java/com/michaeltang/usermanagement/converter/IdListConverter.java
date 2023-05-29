package com.michaeltang.usermanagement.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.michaeltang.usermanagement.common.exception.ValidationException;

public class IdListConverter extends ConverterDecorator {

	private Pattern p = Pattern.compile("^[a-zA-Z0-9, ]+$");
    public IdListConverter(Converter instance) {
        super(instance);
    }

    @Override
    public Object doConvert(Object entity) throws ValidationException {
        final String idList = (String) entity;
        if (idList == null || idList.isEmpty()) {
            throw new ValidationException("empty user id");
        }
        Matcher m = p.matcher(idList);
        if (!m.matches()) {
        	throw new ValidationException("Invalid userId");
        }
        String[] list = ((String) entity).split(",");
        return list;
    }

}