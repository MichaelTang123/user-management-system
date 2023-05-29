package com.michaeltang.usermanagement.callback;

import com.michaeltang.usermanagement.common.model.RegistrationEvent;
import com.michaeltang.usermanagement.common.model.User;

/**
 * The callbacks that called after the event is produced.
 * @author tangyh
 *
 */
public interface EventCallback {
	/**
	 * Called when user is registered successfully.
	 * @param user the user that registered
	 */
	void onUserRegistered(RegistrationEvent event);
	
	/**
	 * Called when user registration has failed.
	 * @param user
	 * @param error
	 */
	void onUserRegistrationError(User user, Object error);
}
