package com.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.shop.models.Page;
import com.shop.repositery.PageRepository;

@Controller
@RequestMapping("/")
public class PageController {

	@Autowired
	private PageRepository pageRepository;

	@GetMapping
	public String home(Model model) {

		Page page = pageRepository.findBySlug("home");
		model.addAttribute("page", page);

		return "page";
	}
	
	@GetMapping("/login")
	public String login() {
		
		return "login";
	}
	
	@GetMapping("/{slug}")
	public String page(@PathVariable String slug, Model model) {

		Page page = pageRepository.findBySlug(slug);
		
		if(page == null) {
			return "redirect:/";
		}
		model.addAttribute("page", page);

		return "page";
	}
}