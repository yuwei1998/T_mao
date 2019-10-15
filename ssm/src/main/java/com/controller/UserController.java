package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.form.User;
import com.service.UserService;

@Controller
@RequestMapping("/admin")
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/listUser")
	public String list(Model model) {
		List<User> users = userService.list();
		model.addAttribute("users", users);
		return "pages/admin/listUser";
	}

	@RequestMapping("/editUser")
	public String edit(Model model, Integer id) {
		User user = userService.get(id);
		model.addAttribute("user", user);
		return "pages/admin/editUser";
	}

	@RequestMapping("/updateUser")
	public String update(Integer id, String password) {
		userService.updatePassword(id, password);
		return "redirect:listUser";
	}
}
