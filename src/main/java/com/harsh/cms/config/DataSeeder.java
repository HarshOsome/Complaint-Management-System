package com.harsh.cms.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.harsh.cms.model.User;
import com.harsh.cms.repository.UserRepository;

@Component
class DataSeeder implements CommandLineRunner{
	private final  UserRepository userRepository;
	
	public DataSeeder(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	@Override
	public void run(String... args) {
		User admin = new User();
		admin.setName("Admin_User");
		admin.setEmail("admin@cms.com");
		admin.setPassword("admin123");
		admin.setRole("ADMIN");
		
		userRepository.save(admin);
		
		
		User employee = new User();
		employee.setName("John_Employee");
		employee.setEmail("john@cms.com");
		employee.setPassword("emp12345");
		employee.setRole("EMPLOYEE");
		employee.setAvailable(true);
		userRepository.save(employee);
		
		User employee2 = new User();
		employee2.setName("Sara_Employee");
		employee2.setEmail("sara@cms.com");
		employee2.setPassword("emp12345");
		employee2.setRole("EMPLOYEE");
		employee2.setAvailable(true);
		userRepository.save(employee2);
		
		User customer = new User();
		customer.setName("Test Customer");
		customer.setEmail("customer@cms.com");
		customer.setPassword("cust1234");
		customer.setRole("CUSTOMER");
		userRepository.save(customer);
	}
	
	
}
