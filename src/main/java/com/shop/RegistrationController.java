package com.shop;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.models.User;
import com.shop.repositery.UserRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping
	public String register(User user) {

		return "register";
	}

	@PostMapping
	public String register(@Valid User user, BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {

			return "register";
		}

		if (!user.getPassword().equals(user.getConfirmPassword())) {

			model.addAttribute("passwordMatchProblem", "password do not match!");
			return "register";
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);

		return "redirect:/login";
	}
}
