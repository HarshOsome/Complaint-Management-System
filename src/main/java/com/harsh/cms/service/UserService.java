package com.harsh.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harsh.cms.exception.InvalidRequestException;
import com.harsh.cms.exception.UserNotFoundException;
import com.harsh.cms.model.User;
import com.harsh.cms.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;

	public User registerUser(User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) { // check if email present if present then:
			throw new InvalidRequestException("Email already registered: " + user.getEmail());
		}
		// edge cases since not validated in model class.
		if (user.getRole() == null || user.getRole().isBlank()) {
			user.setRole("CUSTOMER");
		}
		// save the user
		return userRepository.save(user);
	}

	public User loginUser(String email, String password) {
		User user = userRepository.findByEmail(email) // find user
				.orElseThrow(() -> new UserNotFoundException("No Account for email : " + email));
		// check for password
		if (!user.getPassword().equals(password)) {
			throw new InvalidRequestException("Invalid password");
		}
		// return the user in login
		return user;
	}

	public User resetPassword(String email, String newPassword) {
		User user = userRepository.findByEmail(email) // find or no account
				.orElseThrow(() -> new UserNotFoundException("No Account found for email: " + email));
		// set the password
		user.setPassword(newPassword);

		return userRepository.save(user);
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not Found with id" + id));

	}

	public List<User> getUsersByRole(String role) {
		return userRepository.findByRole(role);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public void deleteUser(Long id) {
		User user = getUserById(id);
		userRepository.delete(user);
	}

}
