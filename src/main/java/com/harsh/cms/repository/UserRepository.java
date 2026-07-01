package com.harsh.cms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.harsh.cms.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
	
	List<User> findByRole(String role);
	
	List<User> findByRoleAndAvailable(String role, boolean available);
	
	
}
