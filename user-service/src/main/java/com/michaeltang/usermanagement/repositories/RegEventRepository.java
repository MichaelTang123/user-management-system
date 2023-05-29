package com.michaeltang.usermanagement.repositories;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.michaeltang.usermanagement.common.constants.Common;
import com.michaeltang.usermanagement.common.model.RegistrationEvent;

public interface RegEventRepository extends CrudRepository<RegistrationEvent, Long> {
	@Async(Common.WORKER_GROUP_DB)
    @Query("SELECT e FROM RegistrationEvent e WHERE e.id = :id")
	CompletableFuture<RegistrationEvent> findEventById(@Param("id") Long id);

    @Async(Common.WORKER_GROUP_DB)
    CompletableFuture<Page<RegistrationEvent>> findAllBy(Pageable pageable);
}