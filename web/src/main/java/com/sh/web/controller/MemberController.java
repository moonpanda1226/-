package com.sh.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sh.web.service.MemberService;
import com.sh.web.util.Util;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	@Autowired
	private Util util;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@PostMapping("/login")
	public String login2(@RequestParam Map<String, Object> map) {
		System.out.println(map);//{id=poseidon, pw=01234567}
		Map<String, Object> result = memberService.login(map);
		System.out.println(result);//{count=1, mname=포세이돈}
		
		if(util.str2Int(result.get("count")) == 1) {
			//정상 - 세션 -> board로 이동
			HttpSession session = util.getSession();
			session.setAttribute("mid", map.get("id"));
			session.setAttribute("mname", result.get("mname"));
			session.setAttribute("mgrade", result.get("mgrade"));
			return "redirect:/freeboard";
		} else {
			//로그인 불가 -> 화면이동 다시 로그인으로 
			return "redirect:/login";
		}
	}
	/* login logout  
	 * 
	 * logon logoff 
	 * 
	 * sign in sign off 
	 * 
	 * */
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		if(session.getAttribute("mid") != null) {
			session.removeAttribute("mid");
		}
		if(session.getAttribute("mname") != null) {
			session.removeAttribute("mname");
			
		}
		session.invalidate();
		
		return "redirect:/login";
	}
	
}
	
	
	