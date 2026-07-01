package com.harsh.cms.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harsh.cms.exception.ComplaintNotFoundException;
import com.harsh.cms.exception.InvalidRequestException;
import com.harsh.cms.exception.UserNotFoundException;
import com.harsh.cms.model.Complaint;
import com.harsh.cms.model.User;
import com.harsh.cms.repository.ComplaintRepository;
import com.harsh.cms.repository.UserRepository;

@Service
public class AssignationService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ComplaintRepository complaintRepository;
	
	public List<User> getAvailable(){
		return userRepository.findByRoleAndAvailable("EMPLOYEE", true);
	}
	
	public Complaint assignComplaint(Long complaintId, Long employeeId) { // manual algorithm
		// throw exception if not exist by Id
		Complaint complaint = complaintRepository.findById(complaintId)
				.orElseThrow(()-> new ComplaintNotFoundException("Complaint not found with id "+complaintId));
		
		// find employee to assign
		User employee  = userRepository.findById(employeeId)
				.orElseThrow(()-> new UserNotFoundException("Employee not found with id: " +employeeId));
	
		if(!"EMPLOYEE".equals(employee.getRole())){
			throw new InvalidRequestException("User with id" + employeeId + " is not an employee");
			
		}
		// assign 
		complaint.setAssignedEmployee(employee);
		complaint.setStatus("ASSIGNED");
		
		return complaintRepository.save(complaint);
	}
	
	public Complaint autoAssignComplaint(Long complaintId) {  // least busy employee algorithm
		List<User> availableEmployees = getAvailable();  // employees who are available.
		
		if(availableEmployees.isEmpty()) {  // edge case
			
			throw new InvalidRequestException("No available employees to assign this complaint");
		}
		
		User leastBusyEmployee = availableEmployees.get(0); // assuming first one is least busy.
		int fewestComplaints = complaintRepository.findByAssignedEmployee_Id(leastBusyEmployee.getId()).size();
		// size of his complaints
		
		for(User employee: availableEmployees) { // iterate among all
			int currentLoad  = complaintRepository.findByAssignedEmployee_Id(employee.getId()).size(); 
			if(currentLoad < fewestComplaints) {
				fewestComplaints = currentLoad;
				leastBusyEmployee = employee;
			}	
		}
		return assignComplaint(complaintId, leastBusyEmployee.getId());
		// anybody with least complaints : assigned first.
	}
	
}
