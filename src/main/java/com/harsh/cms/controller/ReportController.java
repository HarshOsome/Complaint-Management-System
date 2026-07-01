package com.harsh.cms.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.harsh.cms.model.Complaint;
import com.harsh.cms.service.ComplaintService;

@Controller
@RequestMapping("/reports")
public class ReportController {
	
	@Autowired
	private ComplaintService complaintService;
	
	@GetMapping
	public String showReportPage() {
		return "reports";
	}
	//5.1
	@GetMapping("/pending")
	public String pendingReport(Model model) {
		List<Complaint> complaints = complaintService.getComplaintsByStatus("PENDING"); // 
		
		model.addAttribute("complaints", complaints);
		model.addAttribute("reportTitle", "Pending Complaints Report");
		
		return "report-result";
		
	}
	//5.2
	@GetMapping("/date-range")
	public String dateRangeReport(@RequestParam String startDate, @RequestParam String endDate , Model model) {
		
		LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
		LocalDateTime end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
		
		List<Complaint> complaints = complaintService.getComplaintsByDateRange(start, end);
		
		model.addAttribute("complaints", complaints);
		model.addAttribute("reportTitle", "Complaints from "+startDate +"to"+endDate);
		
		return "report-result";
	}
	//5.3
	@GetMapping("/solved")
	public String solvedReport(Model model) {
		List<Complaint> complaints = complaintService.getComplaintsByStatus("COMPLETED");
		
		model.addAttribute("complaints", complaints);
		model.addAttribute("reportTitle", "Solved Complaints Report");
		
		return "report-result";
	}
	
	//5.4
	@GetMapping("/user/{customerId}")
	public String userWiseReport(@PathVariable Long customerId, Model model) {
		List<Complaint> complaints = complaintService.getComplaintsByCustomer(customerId);
		
		model.addAttribute("complaints", complaints);
		model.addAttribute("reportTitle","Complaints for User #" +customerId);
		return "report-result";
	}
	
	
	
	
}

