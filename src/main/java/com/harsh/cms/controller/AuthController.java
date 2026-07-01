package com.harsh.cms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.harsh.cms.model.User;
import com.harsh.cms.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String showLogingPage() {
		return "login";
	}

	@PostMapping("/login")
	public String loginUser(@RequestParam String email, @RequestParam String password,@RequestParam String loginType, Model model,
			HttpSession session) {

		try {
			User user = userService.loginUser(email, password);
			// Version 3 changes:
			if (!user.getRole().equals(loginType)) {
			    model.addAttribute("error",
			            "Access denied. Please login using the correct portal.");
			    return "login";
			}
			
			session.setAttribute("loggedInUser", user);

			switch (user.getRole()) {
			case "ADMIN":
				return "redirect:/admin/dashboard";

			case "EMPLOYEE":
				return "redirect:/employee/dashboard";
			default:
				return "redirect:/customer/dashboard";
			}
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, // if not valid - redisplay the form rather
																			// than crashing.
			BindingResult result, Model model) { // trigger to all the @notblank etc.

		// if validations crash, go to register,
		if (result.hasErrors()) {
			return "register";
		}

		try {
			userService.registerUser(user);
			return "redirect:/login";

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "register";
		}

	}

	@GetMapping("/forget-password")
	public String showForgetPasswordPage() {
		return "forget-password";
	}

	@PostMapping("/forget-password")
	public String resetPassword(@RequestParam String email, @RequestParam String newPassword, Model model) {

		try {
			userService.resetPassword(email, newPassword);
			model.addAttribute("message", "Password reset successful. Please login");
			return "login";

		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "forget-password";
		}
	}
}
