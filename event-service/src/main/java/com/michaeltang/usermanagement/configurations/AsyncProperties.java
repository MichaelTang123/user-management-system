package com.michaeltang.usermanagement.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Thread pool properties for worker groups including request-dispatcher and IO-access group.
 * TODO: we can separate them into different configurations.
 * @author tangyh
 *
 */
@Configuration
@ConfigurationProperties(prefix = "async", ignoreUnknownFields = false)
public class AsyncProperties {

	private Integer corePoolSizeCommon = 3;
	private Integer maxPoolSizeCommon = 5;
	private Integer queueCapacityCommon = 1000;
	private Integer corePoolSizeStoreRead = 5;
	private Integer maxPoolSizeStoreRead = 10;
	private Integer queueCapacityStoreRead = 1000;
	private Integer corePoolSizeReqRead = 5;
	private Integer maxPoolSizeReqRead = 10;
	private Integer queueCapacityReqRead = 1000;
	private Integer corePoolSizeReqWrite = 5;
	private Integer maxPoolSizeReqWrite = 10;
	private Integer queueCapacityReqWrite = 1000;
	private Integer corePoolSizeEmail = 5;
	private Integer maxPoolSizeEmail = 10;
	private Integer queueCapacityEmail = 1000;
	private Integer timeout = 1000;

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Integer getCorePoolSizeStoreRead() {
		return corePoolSizeStoreRead;
	}

	public void setCorePoolSizeStoreRead(Integer corePoolSizeStoreRead) {
		this.corePoolSizeStoreRead = corePoolSizeStoreRead;
	}

	public Integer getMaxPoolSizeStoreRead() {
		return maxPoolSizeStoreRead;
	}

	public void setMaxPoolSizeStoreRead(Integer maxPoolSizeStoreRead) {
		this.maxPoolSizeStoreRead = maxPoolSizeStoreRead;
	}

	public Integer getQueueCapacityStoreRead() {
		return queueCapacityStoreRead;
	}

	public void setQueueCapacityStoreRead(Integer queueCapacityStoreRead) {
		this.queueCapacityStoreRead = queueCapacityStoreRead;
	}

	public Integer getCorePoolSizeReqRead() {
		return corePoolSizeReqRead;
	}

	public void setCorePoolSizeReqRead(Integer corePoolSizeReqRead) {
		this.corePoolSizeReqRead = corePoolSizeReqRead;
	}

	public Integer getMaxPoolSizeReqRead() {
		return maxPoolSizeReqRead;
	}

	public void setMaxPoolSizeReqRead(Integer maxPoolSizeReqRead) {
		this.maxPoolSizeReqRead = maxPoolSizeReqRead;
	}

	public Integer getQueueCapacityReqRead() {
		return queueCapacityReqRead;
	}

	public void setQueueCapacityReqRead(Integer queueCapacityReqRead) {
		this.queueCapacityReqRead = queueCapacityReqRead;
	}

	public Integer getCorePoolSizeReqWrite() {
		return corePoolSizeReqWrite;
	}

	public void setCorePoolSizeReqWrite(Integer corePoolSizeReqWrite) {
		this.corePoolSizeReqWrite = corePoolSizeReqWrite;
	}

	public Integer getMaxPoolSizeReqWrite() {
		return maxPoolSizeReqWrite;
	}

	public void setMaxPoolSizeReqWrite(Integer maxPoolSizeReqWrite) {
		this.maxPoolSizeReqWrite = maxPoolSizeReqWrite;
	}

	public Integer getQueueCapacityReqWrite() {
		return queueCapacityReqWrite;
	}

	public void setQueueCapacityReqWrite(Integer queueCapacityReqWrite) {
		this.queueCapacityReqWrite = queueCapacityReqWrite;
	}

	public Integer getCorePoolSizeCommon() {
		return corePoolSizeCommon;
	}

	public void setCorePoolSizeCommon(Integer corePoolSizeCommon) {
		this.corePoolSizeCommon = corePoolSizeCommon;
	}

	public Integer getMaxPoolSizeCommon() {
		return maxPoolSizeCommon;
	}

	public void setMaxPoolSizeCommon(Integer maxPoolSizeCommon) {
		this.maxPoolSizeCommon = maxPoolSizeCommon;
	}

	public Integer getQueueCapacityCommon() {
		return queueCapacityCommon;
	}

	public void setQueueCapacityCommon(Integer queueCapacityCommon) {
		this.queueCapacityCommon = queueCapacityCommon;
	}

	public Integer getCorePoolSizeEmail() {
		return corePoolSizeEmail;
	}

	public void setCorePoolSizeEmail(Integer corePoolSizeEmail) {
		this.corePoolSizeEmail = corePoolSizeEmail;
	}

	public Integer getMaxPoolSizeEmail() {
		return maxPoolSizeEmail;
	}

	public void setMaxPoolSizeEmail(Integer maxPoolSizeEmail) {
		this.maxPoolSizeEmail = maxPoolSizeEmail;
	}

	public Integer getQueueCapacityEmail() {
		return queueCapacityEmail;
	}

	public void setQueueCapacityEmail(Integer queueCapacityEmail) {
		this.queueCapacityEmail = queueCapacityEmail;
	}
}