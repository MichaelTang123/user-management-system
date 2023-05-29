package com.michaeltang.usermanagement.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "email", ignoreUnknownFields = false)
public class EventProcProperties {
	private int scanEventInterval = 3000;
	private String smtpServer;
	private int smtpPort = 587;
	private String accountName;
	private String accountPwd;
	private String emailFrom = "noreply@michaeltang.com";
	private String subject = "User registration";
	public int getScanEventInterval() {
		return scanEventInterval;
	}
	public void setScanEventInterval(int scanEventInterval) {
		this.scanEventInterval = scanEventInterval;
	}
	public String getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(String smtpServer) {
		this.smtpServer = smtpServer;
	}
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public int getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountPwd() {
		return accountPwd;
	}
	public void setAccountPwd(String accountPwd) {
		this.accountPwd = accountPwd;
	}
}
