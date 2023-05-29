package com.michaeltang.usermanagement.converter;

import com.michaeltang.usermanagement.common.exception.ValidationException;

public abstract class ConverterDecorator implements Converter {
    private Converter next;
    
    protected ConverterDecorator(Converter instance) {
        this.next = instance;
    }
    
    public Object convert(Object entity) throws ValidationException {
        if (next != null) {
            return doConvert(next.convert(entity));
        } else {
        	return doConvert(entity);
        }
    }
    
    protected abstract Object doConvert(Object entity) throws ValidationException;
}
