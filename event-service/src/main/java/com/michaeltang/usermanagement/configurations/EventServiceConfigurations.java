package com.michaeltang.usermanagement.configurations;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.michaeltang.usermanagement.common.model.RegistrationEvent;
import com.michaeltang.usermanagement.converter.Converter;
import com.michaeltang.usermanagement.converter.IdListConverter;
import com.michaeltang.usermanagement.converter.TrimConverter;
import com.michaeltang.usermanagement.services.EventService;


@Configuration
@AutoConfigureAfter(CommonConfigurations.class)
@EnableConfigurationProperties(EventProcProperties.class)
public class EventServiceConfigurations {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Bean(name = "idListConverter")
    public Converter idListConverter() {
        Converter idListConverter = new IdListConverter(null);
        Converter trimConverter = new TrimConverter(idListConverter);
        return trimConverter;
    }
    
    @Autowired
    private EventProcProperties eventProcProperties;
    
    @Autowired
    private EventService eventService;
    
    private ScheduledExecutorService executorService = null;
    
    @PostConstruct
    public void onApplicationEvent() {
    	executorService = Executors.newSingleThreadScheduledExecutor();
    	try {
    		synchronized (this) {
		    	logger.info("Event dispatch worker started, scanEventInterval: {}", eventProcProperties.getScanEventInterval());
		    	executorService.scheduleAtFixedRate(() -> {
		    		final List<RegistrationEvent> events = eventService.markPendingEvents();
		    		if (events.size() > 0) {
		    			eventService.processEvent(events);
		    		}
		    	}, 1000, eventProcProperties.getScanEventInterval(), TimeUnit.MILLISECONDS);
    		}
    	} catch (Exception e) {
    		logger.error("Failed to mark pending events", e);
    	}
    }
    
    @PreDestroy
    public void destroy() {
    	executorService.shutdown();
    	try {
			executorService.awaitTermination(5000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error("Failed to terminate workers", e);
		}
    }
}
