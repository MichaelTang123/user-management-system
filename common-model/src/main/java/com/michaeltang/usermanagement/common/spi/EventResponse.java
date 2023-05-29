package com.michaeltang.usermanagement.common.spi;

import java.util.Date;

import com.michaeltang.usermanagement.common.model.RegistrationEvent;

public class EventResponse {
	private String user;
	
	private Date createDate;
    
    private Date updateDate;
    
    private String email;
    
    /**
     * States:
     *   pending: the initial state
     *   sending: started
     *   failed: fail to send, the detailed error will be recorded in emailError field
     *   complete: completed
     *   @see Common
     */
    private String emailState;
    
    /**
     * Updated when error occurs while sending email
     */
    private String emailError;
    
    public static EventResponse from(RegistrationEvent from) {
    	EventResponse resp = new EventResponse();
    	resp.setUser(from.getUser().getId());
    	resp.setEmail(from.getUser().getEmail());
    	resp.setCreateDate(from.getCreateDate());
    	resp.setUpdateDate(from.getUpdateDate());
    	resp.setEmailState(from.getEmailState());
    	resp.setEmailError(from.getEmailError());
    	return resp;
    }
	
	@Override
    public String toString() {
        return "RegistrationEvent{" +
                "id=" + user +
                ", name='" + user + '\'' +
                '}';
    }

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getEmailState() {
		return emailState;
	}

	public void setEmailState(String emailState) {
		this.emailState = emailState;
	}

	public String getEmailError() {
		return emailError;
	}

	public void setEmailError(String emailError) {
		this.emailError = emailError;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
