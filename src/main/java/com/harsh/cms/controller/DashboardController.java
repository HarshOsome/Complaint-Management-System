package com.harsh.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.harsh.cms.model.Complaint;
import com.harsh.cms.model.User;
import com.harsh.cms.service.AssignationService;
import com.harsh.cms.service.ComplaintService;
import com.harsh.cms.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {

    private final ComplaintController complaintController;
	
	@Autowired
	private ComplaintService complaintService;
	
	@Autowired
	private AssignationService assignationService;
	
	@Autowired
	private UserService userService;

    DashboardController(ComplaintController complaintController) {
        this.complaintController = complaintController;
    }
	
	@GetMapping("/admin/dashboard")
	public String adminDashboard(Model model, HttpSession session) {
		User user = (User) session.getAttribute("loggedInUser");
		if(user == null) {
			return "redirect:/login";
		}
		model.addAttribute("user", user);
		
		List<Complaint> all = complaintService.loadComplaints();
		model.addAttribute("totalComplaints", all.size());
		model.addAttribute("pendingCount", complaintService.getComplaintsByStatus("PENDING").size());
		model.addAttribute("completedCount", complaintService.getComplaintsByStatus("COMPLETED").size());
		model.addAttribute("availableEmployeeCount", assignationService.getAvailable().size());
		return "admin-dashboard";
	}
	
	@GetMapping("/employee/dashboard")
	public String employeeDashboard(Model model, HttpSession session) {
		User sessionUser = (User) session.getAttribute("loggedInUser");
		if(sessionUser == null ) {
			return "redirect:/login";
		}
		
		User user = userService.getUserById(sessionUser.getId());
		model.addAttribute("user", user);
		model.addAttribute("assignedComplaints", complaintService.getComplaintsByEmployee(user.getId()));
		
		return "employee-dashboard";
	}
	
	@GetMapping("/customer/dashboard")
	public String customerDashboard(Model model, HttpSession session) {
		User sessionUser = (User) session.getAttribute("loggedInUser");
		if(sessionUser == null) {
			return "redirect:/login";
		}
		User user = userService.getUserById(sessionUser.getId());
		model.addAttribute("user", user);
		model.addAttribute("myComplaints", complaintService.getComplaintsByCustomer(user.getId()));
		return "customer-dashboard";
	}
}
