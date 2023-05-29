package com.michaeltang.usermanagement.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.michaeltang.usermanagement.callback.EventCallback;
import com.michaeltang.usermanagement.common.model.User;

/**
 * User registration service contract
 * @author tangyh
 *
 */
public interface UserService {
    
    /**
     * Register a new user and save to storage
     * @param user The user to be registered
     * @return The registered user
     * @throws Exception 
     */
	User registerUser(User user) throws Exception;
    
    /**
     * Update existing user, or create if not exists.
     * @param user The user to be updated
     * @return The updated user
     */
	Optional<User> editUser(User user);
    
    /**
     * List pageable users
     * @param paging pageable request parameters.
     * @return The fetched user list
     */
    CompletableFuture<Page<User>> listUsers(final Pageable paging);
    
    /**
     * Query user specified by user name
     * @param name the name of the user.
     * @return The found user
     */
    CompletableFuture<User> queryUser(String name);
    
    /**
     * Delete users by user id list
     * @param the id list of the users to be deleted
     */
    void deleteUser(List<String> idList);

    /**
     * Set callback instance for events handling.
     * @param cb the callback instance
     */
	void setEventCallback(EventCallback cb);
}
