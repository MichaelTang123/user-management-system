package com.michaeltang.usermanagement.repositories;

import java.util.concurrent.CompletableFuture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;

import com.michaeltang.usermanagement.common.constants.Common;
import com.michaeltang.usermanagement.common.model.User;

public interface UserRepository extends CrudRepository<User, String> {
	@Async(Common.WORKER_GROUP_DB)
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.deleted = false")
	CompletableFuture<User> findByName(@Param("id") String id);
    
	@Async(Common.WORKER_GROUP_DB)
	@Query("SELECT u FROM User u WHERE u.deleted = false")
	CompletableFuture<Page<User>> findAllBy(Pageable pageable);
	
	@Modifying
	@Query("UPDATE User u SET u.deleted = true WHERE u.id IN :ids")
	void deleteByIds(Iterable<String> ids);
}
