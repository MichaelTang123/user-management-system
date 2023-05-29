package com.michaeltang.usermanagement.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.michaeltang.usermanagement.common.model.RegistrationEvent;

public interface EventService {

    /**
     * List pageable events
     * @param paging pageable request parameters.
     * @return The fetched event list
     */
	CompletableFuture<Page<RegistrationEvent>> listEvents(Pageable paging);


	/**
	 * Event process entry, should be scheduled periodically. 
	 * @param events the events that marked as pending state
	 */
	void processEvent(final List<RegistrationEvent> events);

	/**
	 * Find out the pending events and mark the state pending->sending
	 * @return the pending events
	 */
	List<RegistrationEvent> markPendingEvents();
}
