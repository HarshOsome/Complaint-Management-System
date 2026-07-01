package com.harsh.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.harsh.cms.model.Complaint;
import com.harsh.cms.model.User;
import com.harsh.cms.service.AssignationService;

@Controller
@RequestMapping("/assignation")
public class AssignationController {
	@Autowired
	private AssignationService assignationService;
	
	@GetMapping("/{complaintId}")
	public String showAssignPage(@PathVariable Long complaintId, Model model) {
		List<User> availableEmployees = assignationService.getAvailable();
		model.addAttribute("employees", availableEmployees);
		model.addAttribute("complaintId", complaintId);
		return "assign-complaint";
	}
	
	@PostMapping("/{complaintId}/assign")
	public String assignComplaint(@PathVariable Long complaintId, @RequestParam Long employeeId ,Model model) {
		
			assignationService.assignComplaint(complaintId, employeeId);
			return "redirect:/complaints/" +complaintId;
		
	}
	
	
	//auto-assignment (LeastBusyEmployee)
	@PostMapping("/{complaintId}/auto-assign")
	public String autoAssignComplaint(@PathVariable Long complaintId, Model model) {
		
			Complaint complaint = assignationService.autoAssignComplaint(complaintId);
			return "redirect:/complaints/" + complaint.getId();
		
	}
}

