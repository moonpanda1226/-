package com.sh.web.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@Component
public class Util {
	

	public int str2Int(String str) {
		try {
			return Integer.parseInt(str); // 123A
		} catch (Exception e) {
			return 0;
		}
	}
	
	public int str2Int(Object obj) {
		return str2Int(String.valueOf(obj));
	}

	public int str2Int2(String str) {
		return Integer.parseInt(str);
	}

	// 2024-02-21 psd 웹표준
	public HttpServletRequest req() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = sra.getRequest();
		return request;
	}

	public HttpSession getSession() {
		HttpSession session = req().getSession();
		return session;
	}

	// ip
	public String getIP() {
		HttpServletRequest request = req();
		String ip = request.getHeader("X-FORWARDED-FOR");
		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	// 숫자인지 검사하는 메소드
	public boolean intCheck(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
