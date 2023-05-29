package com.michaeltang.usermanagement.configurations;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@EnableConfigurationProperties(EventProcProperties.class)
public class EmailConfigurations {
	@Autowired
    private EventProcProperties eventProcProperties;

	@Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(eventProcProperties.getSmtpServer());
        mailSender.setPort(eventProcProperties.getSmtpPort());
        mailSender.setUsername(eventProcProperties.getAccountName());
        mailSender.setPassword(eventProcProperties.getAccountPwd());
        
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        
        return mailSender;
    }
}
