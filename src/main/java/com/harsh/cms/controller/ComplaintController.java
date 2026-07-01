package com.harsh.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.harsh.cms.model.Complaint;
import com.harsh.cms.model.User;
import com.harsh.cms.service.ComplaintService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/complaints")
public class ComplaintController {
	@Autowired
	private ComplaintService complaintService;

	@GetMapping
	public String viewAllComplaints(Model model, HttpSession session) {

		User loggedInUser = (User) session.getAttribute("loggedInUser");

		if (loggedInUser == null) {
			return "redirect:/login";
		}

		List<Complaint> complaints;

		if ("ADMIN".equals(loggedInUser.getRole())) {
			complaints = complaintService.loadComplaints();
		} else if ("EMPLOYEE".equals(loggedInUser.getRole())) {
			complaints = complaintService.getComplaintsByEmployee(loggedInUser.getId());
		} else {
			complaints = complaintService.getComplaintsByCustomer(loggedInUser.getId());
		}

		model.addAttribute("complaints", complaints);
		return "complaint-list";
	}

	@GetMapping("/new")
	public String showAddComplaintForm(Model model, HttpSession session) {
		model.addAttribute("complaint", new Complaint()); // add a new complaint

		User loggedInUser = (User) session.getAttribute("loggedInUser"); // wrapping it to User // set session

		model.addAttribute("userRole", loggedInUser != null ? loggedInUser.getRole() : "CUSTOMER"); // if new then
																									// automatically
																									// it's customer or
																									// get role.

		return "complaint-form";
	}

	@GetMapping("/{id}/edit")
	public String showEditComplaintForm(@PathVariable Long id, Model model, HttpSession session) {
		Complaint complaint = complaintService.getComplaintById(id);

		model.addAttribute("complaint", complaint);

		User loggedInUser = (User) session.getAttribute("loggedInUser"); // saving session for user.

		model.addAttribute("userRole", loggedInUser != null ? loggedInUser.getRole() : "CUSTOMER");
		return "complaint-form";
	}

	@PostMapping
	public String addComplaint(@Valid @ModelAttribute("complaint") Complaint complaint, BindingResult result,
			Model model, HttpSession session) {

		if (result.hasErrors()) {
			return "complaint-form";
		}

		User loggedInUser = (User) session.getAttribute("loggedInUser");

		if (loggedInUser != null && "CUSTOMER".equals(loggedInUser.getRole())) {
			complaint.setCustomer(loggedInUser);
		}

		try {
			complaintService.addComplaint(complaint);

			if (loggedInUser != null) {
				if ("ADMIN".equals(loggedInUser.getRole())) {
					return "redirect:/complaints";
				} else if ("EMPLOYEE".equals(loggedInUser.getRole())) {
					return "redirect:/employee/dashboard";
				}
			}

			return "redirect:/customer/dashboard";

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "complaint-form";
		}

	}

	@GetMapping("/{id}")
	public String viewComplaint(@PathVariable Long id, Model model) {
		Complaint complaint = complaintService.getComplaintById(id);
		model.addAttribute("complaint", complaint);
		return "complaint-detail";
	}

	@PostMapping("/{id}")
	public String updateComplaint(@PathVariable Long id, @Valid @ModelAttribute("complaint") Complaint complaint,
			BindingResult result, Model model) {

		if (result.hasErrors()) {
			return "complaint-form";
		}

		try {
			complaintService.updateComplaint(id, complaint); // update complaints
			return "redirect:/complaints";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "complaint-form";
		}
	}

	@PostMapping("/{id}/status")
	public String updateStatus(@PathVariable Long id, @RequestParam String status,
			@RequestParam(required = false) String remarks, Model model) {
		try {
			Complaint complaint = complaintService.getComplaintById(id);
			complaint.setStatus(status);

			if (remarks != null) {
				complaint.setRemarks(remarks);
			}

			complaintService.updateComplaint(id, complaint);
			return "redirect:/complaints/" + id;
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "complaint-detail";
		}

	}

	@PostMapping("/{id}/reopen")
	public String reopenComplaint(@PathVariable Long id) {

		Complaint complaint = complaintService.getComplaintById(id);

		complaint.setStatus("PENDING");
		complaint.setAssignedEmployee(null); // remove old employee
		complaint.setRemarks("");

		complaintService.updateComplaint(id, complaint);

		return "redirect:/complaints/" + id;
	}

	@PostMapping("{id}/delete")
	public String deleteComplaint(@PathVariable Long id) {
		complaintService.deleteComplaint(id);
		return "redirect:/complaints";
	}

	@GetMapping("/filter")
	public String filterByStatus(@RequestParam String status, Model model) {
		List<Complaint> complaints = complaintService.getComplaintsByStatus(status);
		model.addAttribute("complaints", complaints);
		return "complaint-list";
	}

	@PostMapping("/{id}/comment")
	public String updateComplaint(@PathVariable Long id, @RequestParam String remarks, HttpSession session) {
		Complaint complaint = complaintService.getComplaintById(id);
		complaint.setRemarks(remarks);
		complaintService.updateComplaint(id, complaint);
		return "redirect:/complaints/" + id;

	}

	@PostMapping("/{id}/feedback")
	public String submitFeedback(@PathVariable Long id, @RequestParam String feedback, HttpSession session) {
		Complaint complaint = complaintService.getComplaintById(id);
		complaint.setFeedback(feedback);
		complaintService.updateComplaint(id, complaint);
		return "redirect:/complaints/" + id;

	}
}
