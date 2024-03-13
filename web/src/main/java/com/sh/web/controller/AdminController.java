package com.sh.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sh.web.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;

	
	@GetMapping("/menu")
	public String menu(Model model) {
		List<Map<String, Object>> menu = adminService.menu();
		model.addAttribute("menu", menu);
		return "admin/menu";
	}
	
	@PostMapping("/menu")
	public String menu(@RequestParam Map<String, Object> map ) {
		System.out.println(map); 
		adminService.menuInsert(map);
		return "redirect:/admin/menu";
	}
}
