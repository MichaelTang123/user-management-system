package com.michaeltang.usermanagement.configurations;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.michaeltang.usermanagement.repositories.RegEventRepository;
import com.michaeltang.usermanagement.services.EventService;
import com.michaeltang.usermanagement.services.impl.DefaultEventService;

/**
 * Common dependencies configurations.
 * @author tangyh
 *
 */
@Configuration
public class CommonConfigurations {
    
    private volatile DefaultEventService eventService = null;
    
    @Autowired
    private RegEventRepository regEventRepository;
    
    @Bean
    public EventService eventService() {
        return getEventService();
    }
    
    private DefaultEventService getEventService() {
        if (eventService == null) {
            synchronized (this) {
                if (eventService == null) {
                	eventService = new DefaultEventService(regEventRepository);
                }
            }
        }
        return eventService;
    }
}
