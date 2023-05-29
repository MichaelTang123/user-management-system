package com.michaeltang.usermanagement.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.michaeltang.usermanagement.repositories.UserRepository;
import com.michaeltang.usermanagement.repositories.RegEventRepository;
import com.michaeltang.usermanagement.services.UserService;
import com.michaeltang.usermanagement.services.impl.DefaultUserService;

/**
 * Common dependencies configurations.
 * @author tangyh
 *
 */
@Configuration
public class CommonConfigurations {
    
    private volatile DefaultUserService userService = null;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RegEventRepository regEventRepository;
    
    @Bean
    public UserService userService() {
        return getUserService();
    }
    
    private UserService getUserService() {
        if (userService == null) {
            synchronized (this) {
                if (userService == null) {
                	userService = new DefaultUserService(userRepository, regEventRepository);
                }
            }
        }
        return userService;
    }
}
