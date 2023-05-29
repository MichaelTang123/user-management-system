package com.michaeltang.usermanagement.services.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.michaeltang.usermanagement.callback.EventCallback;
import com.michaeltang.usermanagement.common.constants.Common;
import com.michaeltang.usermanagement.common.model.RegistrationEvent;
import com.michaeltang.usermanagement.common.model.User;
import com.michaeltang.usermanagement.repositories.RegEventRepository;
import com.michaeltang.usermanagement.repositories.UserRepository;
import com.michaeltang.usermanagement.services.UserService;

public class DefaultUserService implements UserService {
	private static Logger logger = LoggerFactory.getLogger(DefaultUserService.class);
    private UserRepository userRepository;
    private RegEventRepository regEventRepository;
    private EventCallback eventCallback = null;
    
    public DefaultUserService(UserRepository userRepository, RegEventRepository regEventRepository) {
        this.userRepository = userRepository;
        this.regEventRepository = regEventRepository;
    }
    
    @Override
    public void setEventCallback(EventCallback cb) {
    	this.eventCallback = cb;
    }

    @Override
    public CompletableFuture<Page<User>> listUsers(final Pageable paging) {
    	CompletableFuture<Page<User>> res = userRepository.findAllBy(paging);
    	return res.thenApply(processFetchedUserList);
    }
    
    @Override
	public CompletableFuture<User> queryUser(String name) {
    	CompletableFuture<User> res = userRepository.findByName(name);
    	return res.thenApply(processFetchedUser.apply(name));
	}
    
    @Override
    @Transactional(rollbackFor = { Exception.class })
	public User registerUser(User user) throws Exception {
    	User res = null;
    	try {
	    	if (userRepository.existsById(user.getId())) {
	    		logger.error("Failed to register user {}, the id has alredy been registered" + user.getId());
	    		throw new SQLException("duplicate userid");
	    	}
	    	user.setDeleted(false);
	    	final Date now = getCurrentDate();
	    	user.setCreateDate(now);
	    	user.setUpdateDate(now);
	    	res = userRepository.save(user);
	    	final RegistrationEvent event = createRegEvent(res);
	    	regEventRepository.save(event);
	    	if (eventCallback != null) {
	    		eventCallback.onUserRegistered(event);
	    	}
    	} catch (Exception e) {
    		logger.error("Failed to register user " + user.getId(), e);
    		if (eventCallback != null) {
    			eventCallback.onUserRegistrationError(user, e);
    		}
    		throw e;
    	}
    	return res;
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public Optional<User> editUser(User user) {
		Optional<User> u = Optional.empty();
		try {
			u = userRepository.findById(user.getId());
	        if (u.isPresent()) {
	        	/**
	        	 * This is white-list of editable fields. Fields outside of the white-list cannot be edited.
	        	 * The only way to change the soft delete flag is to update via (soft) delete API 
	        	 */
	        	u.get().setUpdateDate(new Date(System.currentTimeMillis()));
	        	u.get().setFirstName(user.getFirstName());
	        	u.get().setLastName(user.getLastName());
	        	u.get().setEmail(user.getEmail());
	        	userRepository.save(u.get());
	        }
		} catch (Exception e) {
			logger.error("Failed to edit user " + user.getId(), e);
			throw e;
		}
        return u;
	}

	@Override
	@Transactional(rollbackFor = { SQLException.class })
	public void deleteUser(List<String> idList) {
		try {
			userRepository.deleteByIds(idList);
		} catch (Exception e) {
			logger.error("Failed to delete users ", e);
			throw e;
		}
	}
	
	private RegistrationEvent createRegEvent(User user) {
		final RegistrationEvent event = new RegistrationEvent();
		event.setUser(user);
		event.setEmailState(Common.EMAIL_PENDING);
		event.setCreateDate(user.getCreateDate());
		event.setUpdateDate(user.getUpdateDate());
		return event;
	}
	
	private Date getCurrentDate() {
		return new Date(System.currentTimeMillis());
	}
    
    private static Function<Page<User>, Page<User>> processFetchedUserList = users -> {
    	logger.info("[Service][{}]Retrieved users: {}", Thread.currentThread(), users.toString());
    	users.forEach(user -> {
    		logger.info("[Service][{}]Retrieved user: {}", Thread.currentThread(), user.getId());
        });
    	return users; 
    };
    
    private static Function<String, Function<User, User>> processFetchedUser = name -> user -> {
    	if (user != null) {
    		logger.info("[Service][{}]Retrieved users: {}", Thread.currentThread(), user.toString());
    	} else {
    		logger.error("Failed to fetch user");
    	}
    	return user; 
    };
}
