package com.michaeltang.usermanagement.configurations;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.michaeltang.usermanagement.common.constants.Common;


/**
 * Configurable Thread pool for worker groups including request-Read/Write, Store-query
 *  and email-notification group etc.
 * @author tangyh
 *
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties(AsyncProperties.class)
public class AsyncConfiguration implements AsyncConfigurer {
	private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AsyncProperties asyncProperties;
    
    @Bean(name = Common.WORKER_GROUP_COMMON)
    public Executor commonExecutor() {
        return createThreadPool(
        		Common.WORKER_GROUP_COMMON,
        		asyncProperties.getCorePoolSizeCommon(),
        		asyncProperties.getMaxPoolSizeCommon(),
        		asyncProperties.getQueueCapacityCommon());
    }

    @Bean(name = Common.WORKER_GROUP_READ_REQ)
    public Executor readRequestExecutor() {
        return createThreadPool(Common.WORKER_GROUP_READ_REQ,
        		asyncProperties.getCorePoolSizeReqRead(),
        		asyncProperties.getMaxPoolSizeReqRead(),
        		asyncProperties.getQueueCapacityReqRead());
    }
    
    @Bean(name = Common.WORKER_GROUP_WRITE_REQ)
    public Executor writeRequestExecutor() {
        return createThreadPool(Common.WORKER_GROUP_WRITE_REQ,
        		asyncProperties.getCorePoolSizeReqWrite(),
        		asyncProperties.getMaxPoolSizeReqWrite(),
        		asyncProperties.getQueueCapacityReqWrite());
    }

    @Bean(name = Common.WORKER_GROUP_DB)
    public Executor dbExecutor() {
        return createThreadPool(
        		Common.WORKER_GROUP_DB,
        		asyncProperties.getCorePoolSizeStoreRead(),
        		asyncProperties.getMaxPoolSizeStoreRead(),
        		asyncProperties.getQueueCapacityStoreRead());
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    private Executor createThreadPool(final String threadNamePrefix,
    		final int corePoolSize,
    		final int maxPoolSize,
    		final int queueCapacity) {
    	logger.info("Creating ThreadPool for {}, corePoolSize: {}, maxPoolSize: {}, queue capacity: {}",
    			threadNamePrefix,
    			corePoolSize,
    			maxPoolSize,
    			queueCapacity);
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix + "-");
        executor.setTaskDecorator(new TaskDecorator() {
			@Override
			public Runnable decorate(Runnable runnable) {
				try {
					final RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
	                return () -> {
	                    try {
	                        RequestContextHolder.setRequestAttributes(attrs);
	                        runnable.run();
	                    } finally {
	                        RequestContextHolder.resetRequestAttributes();
	                    }
	                };
	            } catch (Exception e) {
	            	logger.error("Faied to set task decorator", e);
	                return runnable;
	            }
			}
        	
        });
        return executor;
    }
}
