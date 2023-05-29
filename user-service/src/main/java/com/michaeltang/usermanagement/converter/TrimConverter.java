package com.michaeltang.usermanagement.converter;

import java.util.ArrayList;
import java.util.List;

import com.michaeltang.usermanagement.common.exception.ValidationException;

public class TrimConverter extends ConverterDecorator {
	private final static int MAX_USR_LIST_SIZE = 10;
	
    public TrimConverter(Converter instance) {
        super(instance);
    }

    @Override
    public Object doConvert(Object entity) throws ValidationException {
    	List<String> res = new ArrayList<>();
    	for (String id : (String[]) entity) {
    		String s = id.trim();
    		if (!s.isEmpty()) {
    			res.add(s);
    			if (res.size() == MAX_USR_LIST_SIZE) {
    				break;
    			}
    		}
    	}
    	if (res.isEmpty()) {
    		throw new ValidationException("empty user list");
    	}
    	return res;
    }
}
