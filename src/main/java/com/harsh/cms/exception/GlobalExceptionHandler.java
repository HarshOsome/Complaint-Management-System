package com.harsh.cms.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice  // watches over all the controllers in the project
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ComplaintNotFoundException.class)
	public String handleComplaintNotFound(ComplaintNotFoundException ex,Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		model.addAttribute("statusCode", 404);
		return "error";
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public String handleUserNotFound(UserNotFoundException ex,Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		model.addAttribute("statusCode", 404);
		return "error";
	}
	
	@ExceptionHandler(InvalidRequestException.class)
	public String handleInvalidRequest(InvalidRequestException ex,Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		model.addAttribute("statusCode", 400);
		return "error";
	}
	
	@ExceptionHandler(Exception.class)
	public String handleGenericException(Exception ex,Model model) {
		model.addAttribute("errorMessage","Something went wrong" +ex.getMessage());
		model.addAttribute("statusCode", 500);
		return "error";
	}
	
	
}
