package com.michaeltang.usermanagement.common.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.michaeltang.usermanagement.common.constants.Common;

@Entity
@Table(name = "RegEvent", 
    indexes = {
        @Index(name = "nameCreateDate", columnList = "createDate", unique = false),
        @Index(name = "nameUpdateDate", columnList = "updateDate", unique = false)
    })
public class RegistrationEvent {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
	private User user;
	
	private Date createDate;
    
    private Date updateDate;
    
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
	
	@Override
    public String toString() {
        return "RegistrationEvent{" +
                "id=" + id +
                ", name='" + id + '\'' +
                '}';
    }

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
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
}
