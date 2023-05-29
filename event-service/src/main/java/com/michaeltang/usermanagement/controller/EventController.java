package com.michaeltang.usermanagement.controller;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.michaeltang.usermanagement.common.constants.Common;
import com.michaeltang.usermanagement.common.model.RegistrationEvent;
import com.michaeltang.usermanagement.common.spi.EventResponse;
import com.michaeltang.usermanagement.services.EventService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


/**
 * The controller for event resources
 * @author tangyh
 *
 */
@RestController("Event Management API")
@RequestMapping("events")
public class EventController {
    public static final String API_ENDPOINT_PATH_EVENTS = "/api/events";
    
    private static Logger logger = LoggerFactory.getLogger(EventController.class);
    
    @Autowired
    private EventService eventService;
    
    /**
	 * Invoke nonblocking DB query in @link UserRepository, running in WORKER_GROUP_READ_REQ thread-pool
	 */
    @Operation(
        summary = "Retrieve events",
        description = "Retrieve event list with pagination",
        tags = {"Events"},
        method = "GET")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
	            array = @ArraySchema(schema = @Schema(implementation = EventResponse.class)))}),
		@ApiResponse(responseCode = "400", content = @Content, description = "Invalid id supplied"), 
		@ApiResponse(responseCode = "404", content = @Content, description = "user not found")})
    @Async(Common.WORKER_GROUP_READ_REQ)
    @RequestMapping(produces = "application/json", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<Page<EventResponse>>> listUsers(@RequestParam(value = "page", defaultValue = "0") int page)
    		throws Exception {
    	logger.info("[Controller][{}]Retrieving user list", Thread.currentThread());
    	return eventService
                .listEvents(PageRequest.of(page, 10, Sort.by("updateDate").descending()))
                .thenApply(handleQueryEventListSuc)
                .exceptionally(handleQueryEventListError);
    }
    
	
	private static Function<Page<RegistrationEvent>, ResponseEntity<Page<EventResponse>>> handleQueryEventListSuc = events -> {
		logger.info("[Controller][{}]Retrieved events: {}", Thread.currentThread(), events.toString());
		final Page<EventResponse> listResp = events.<EventResponse>map(e -> EventResponse.from(e));
		return ResponseEntity.<Page<EventResponse>>ok(listResp);
	};
	
	private static Function<Throwable, ResponseEntity<Page<EventResponse>>> handleQueryEventListError = throwable -> {
		logger.error("Unable to retrieve events", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
