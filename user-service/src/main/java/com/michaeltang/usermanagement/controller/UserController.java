package com.michaeltang.usermanagement.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.michaeltang.usermanagement.common.constants.Common;
import com.michaeltang.usermanagement.common.model.User;
import com.michaeltang.usermanagement.converter.Converter;
import com.michaeltang.usermanagement.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;

/**
 * The controller for user resources
 * @author tangyh
 *
 */
@RestController("User Management API")
@RequestMapping("users")
public class UserController {
    private static Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    @Qualifier("idListConverter")
    private Converter idListConverter;
    
    @Autowired
    private UserService userService;
    

	/**
	 * Invoke nonblocking DB query in @link UserRepository, running in WORKER_GROUP_READ_REQ thread-pool
	 */
    @Operation(
        summary = "Retrieve users",
        description = "Retrieve user list with pagination",
        tags = {"Users"},
        method = "GET")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
	            array = @ArraySchema(schema = @Schema(implementation = User.class)))}),
		@ApiResponse(responseCode = "400", content = @Content, description = "Invalid id supplied"), 
		@ApiResponse(responseCode = "404", content = @Content, description = "user not found")})
    @Async(Common.WORKER_GROUP_READ_REQ)
    @RequestMapping(produces = "application/json", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<Page<User>>> listUsers(@RequestParam(value = "page", defaultValue = "0") int page)
    		throws Exception {
    	logger.info("[Controller][{}]Retrieving user list", Thread.currentThread());
    	return userService
                .listUsers(PageRequest.of(page, 10, Sort.by("updateDate").descending()))
                .<ResponseEntity<Page<User>>>thenApply(handleQueryUserListSuc)
                .exceptionally(handleQueryUserListError);
    }

    /**
	 * Invoke nonblocking DB query, running in WORKER_GROUP_READ_REQ thread-pool
	 */
    @Operation(
        summary = "Query user",
        description = "Query user by user id",
        tags = {"User"},
        method = "GET")
    @ApiResponses(value = { 
	    @ApiResponse(responseCode = "200", content = {@Content(
	    	    mediaType = "application/json", schema = @Schema(implementation = User.class))}),
	    @ApiResponse(responseCode = "400", content = @Content, description = "Invalid id supplied"),
	    @ApiResponse(responseCode = "404", content = @Content, description = "user not found")})
    @Async(Common.WORKER_GROUP_READ_REQ)
    @RequestMapping(value = "/{userId}", produces = "application/json", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<User>> getUser(
    		@PathVariable ("userId")
    		String userId) throws Exception {
    	logger.info("[Controller][{}]Query user: {}", Thread.currentThread(), userId);
    	return userService
                .queryUser(userId)
                .<ResponseEntity<User>>thenApply(handleQueryUserSuc)
                .exceptionally(handleQueryUserError.apply(userId));
    }

    /**
	 * Dispatch write request to WORKER_GROUP_WRITE_REQ thread-pool
	 */
    @Operation(
    	summary = "Register user",
    	description = "Register new user",
    	tags = {"User"},
    	method = "POST")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", content = {@Content(
		    	mediaType = "application/json", schema = @Schema(implementation = User.class))}),
    	@ApiResponse(responseCode = "400", content = @Content, description = "Invalid payload supplied")})
    @Async(Common.WORKER_GROUP_WRITE_REQ)
    @RequestMapping(produces = "application/json", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<User>> registerUser(
    		@io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(
		    	mediaType = "application/json", schema = @Schema(implementation = User.class))})
    		@Valid @RequestBody User user) throws Exception {
        logger.info("[Controller][{}]Registering user: {}", Thread.currentThread(), user.getId());
        final User res = userService.registerUser(user);
        final URI uri = res != null ? ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(user.getId()).toUri() : null; 
        return CompletableFuture.completedFuture(res != null ? ResponseEntity.created(uri).body(res) : ResponseEntity.noContent().build());
    }

    /**
	 * Dispatch write request to WORKER_GROUP_WRITE_REQ thread-pool
	 */
    @Operation(
    	summary = "Edit user",
    	description = "Edit existing user",
    	tags = {"User"},
    	method = "PUT")
    @ApiResponses(value = { 
            @ApiResponse(responseCode = "204", content = @Content),
        	@ApiResponse(responseCode = "400", content = @Content, description = "Invalid payload supplied"), 
        	@ApiResponse(responseCode = "404", content = @Content, description = "user not found")})
    @Async(Common.WORKER_GROUP_WRITE_REQ)
    @RequestMapping(value = "/{userId}", produces = "application/json", method = RequestMethod.PUT)
    public CompletableFuture<ResponseEntity<User>> editUser(
    		@io.swagger.v3.oas.annotations.parameters.RequestBody(content = {@Content(
    		    	mediaType = "application/json", schema = @Schema(implementation = User.class))}) 
    		@Valid @RequestBody User user, 
            @PathVariable ("userId") String userId) throws Exception {
        logger.info("[Controller][{}]Editing user: {}", Thread.currentThread(), user.getId());
        final Optional<User> res = userService.editUser(user);
        return CompletableFuture.completedFuture(
        	res.<ResponseEntity<User>>map(u -> ResponseEntity.noContent().build()).orElse(ResponseEntity.notFound().build())
        );
    }

    /**
	 * Dispatch write request to WORKER_GROUP_WRITE_REQ thread-pool
	 */
    @Operation(
    	summary = "Delete users",
    	description = "Delete existing users by specified user id list",
    	tags = {"Users"},
    	method = "DELETE")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", content = @Content),
        @ApiResponse(responseCode = "400", content = @Content, description = "Invalid id supplied"), 
        @ApiResponse(responseCode = "404", content = @Content, description = "user not found")})
	@Async(Common.WORKER_GROUP_WRITE_REQ)
	@RequestMapping(produces = "application/json", method = RequestMethod.DELETE)
    public CompletableFuture<ResponseEntity> deleteUser(
    		@RequestParam(required = true, value = "ids") String ids) throws Exception {
    	logger.info("[Controller][{}]Deleting user list: {}", Thread.currentThread(), ids);
    	List<String> idList = (List<String>) idListConverter.convert(ids);
    	userService.deleteUser(idList);
    	return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }

	private static Function<Page<User>, ResponseEntity<Page<User>>> handleQueryUserListSuc = userList -> {
		logger.info("[Controller][{}]Retrieved users: {}", Thread.currentThread(), userList.toString());
		return ResponseEntity.ok(userList);
	};

	private static Function<Throwable, ResponseEntity<Page<User>>> handleQueryUserListError = throwable -> {
		logger.error("Unable to retrieve users", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

    private static Function<User, ResponseEntity<User>> handleQueryUserSuc = user -> {
    	if (user != null) {
    		logger.info("[Controller][{}]Query user {} completed", Thread.currentThread(), user.getId());
    	} else {
    		logger.error("[Controller]User not found", Thread.currentThread());
    	}
	    return user != null ? ResponseEntity.<User>ok(user) : ResponseEntity.notFound().build();
	};

	private static Function<String, Function<Throwable, ResponseEntity<User>>> handleQueryUserError = userId -> throwable -> {
        logger.error(String.format("Unable to fetch user for id: %s", userId), throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
