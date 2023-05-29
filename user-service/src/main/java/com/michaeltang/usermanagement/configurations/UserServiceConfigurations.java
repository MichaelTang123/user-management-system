package com.michaeltang.usermanagement.configurations;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.michaeltang.usermanagement.converter.Converter;
import com.michaeltang.usermanagement.converter.IdListConverter;
import com.michaeltang.usermanagement.converter.TrimConverter;

@Configuration
@AutoConfigureAfter(CommonConfigurations.class)
public class UserServiceConfigurations {
    @Bean(name = "idListConverter")
    public Converter idListConverter() {
        Converter idListConverter = new IdListConverter(null);
        Converter trimConverter = new TrimConverter(idListConverter);
        return trimConverter;
    }
}
