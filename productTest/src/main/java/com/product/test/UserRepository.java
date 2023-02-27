package com.product.test;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer>{
	Optional<UserEntity> findByUserId(String userId);
}