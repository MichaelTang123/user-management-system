package com.michaeltang.usermanagement.services.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import com.michaeltang.usermanagement.common.constants.Common;
import com.michaeltang.usermanagement.common.model.RegistrationEvent;
import com.michaeltang.usermanagement.configurations.EventProcProperties;
import com.michaeltang.usermanagement.repositories.RegEventRepository;
import com.michaeltang.usermanagement.services.EventService;

public class DefaultEventService implements EventService {
	private static Logger logger = LoggerFactory.getLogger(DefaultEventService.class);
	
    private RegEventRepository regEventRepository;
    
    @Autowired
    private EventProcProperties eventProcProperties;
    
    @Autowired
    private JavaMailSender emailSender;
    
    public DefaultEventService(RegEventRepository regEventRepository) {
        this.regEventRepository = regEventRepository;
    }
    
    @Override
    public CompletableFuture<Page<RegistrationEvent>> listEvents(final Pageable paging) {
    	CompletableFuture<Page<RegistrationEvent>> res = regEventRepository.findAllBy(paging);
    	return res.thenApply(processFetchedEventList);
    }
    
    @Override
    @Transactional(rollbackFor = { Exception.class })
    public List<RegistrationEvent> markPendingEvents() {
    	final Pageable page = PageRequest.of(0, 10, Sort.by("updateDate").ascending());
    	final List<RegistrationEvent> events = regEventRepository.findPendingEvents(page).getContent();
    	logger.info("[EventService][{}]Event processing scheduled: total {} of pending events found",
    			Thread.currentThread(), events.size());
    	events.forEach(event -> {
    		event.setEmailState(Common.EMAIL_SENDING);
    		regEventRepository.save(event);
        });
    	return events;
    }
    
    @Override
    @Async(Common.WORKER_GROUP_EMAIL)
    @Transactional(rollbackFor = { Exception.class })
    public void processEvent(final List<RegistrationEvent> events) {
    	logger.info("[EventService][{}]Event process scheduled: processing pending events, size of pending events {}",
    			Thread.currentThread(),
    			events.size());
    	events.forEach(event -> {
			final String email = event.getUser().getEmail();
    		try {
	    		final SimpleMailMessage message = new SimpleMailMessage(); 
	            message.setFrom(eventProcProperties.getEmailFrom());
	            message.setTo(email); 
	            message.setSubject(eventProcProperties.getSubject()); 
	            message.setText(event.getUser().getId() + "Welcome to user management system");
	            emailSender.send(message);
	            event.setEmailState(Common.EMAIL_COMPLETED);
    		} catch (Exception e) {
    			logger.error("Failed to send email to " + email, e);
    			event.setEmailState(Common.EMAIL_FAILED);
    		} finally {
    			regEventRepository.save(event);
    		}
        });
    }
    
    private static Function<Page<RegistrationEvent>, Page<RegistrationEvent>> processFetchedEventList = events -> {
    	logger.info("[EventService][{}]Retrieved events: {}", Thread.currentThread(), events.toString());
    	events.forEach(event -> {
    		logger.info("[EventService][{}]Retrieved event: {}", Thread.currentThread(), event.getUser().getId());
        });
    	return events; 
    };
}
