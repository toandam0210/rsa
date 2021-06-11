package com.test.auth.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.auth.model.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	Optional<User> findByUsername(String username);
	Boolean existsByUsername(String username);
}