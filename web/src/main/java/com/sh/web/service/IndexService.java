package com.sh.web.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sh.web.dao.IndexDAO;
import com.sh.web.util.Util;

@Service
public class IndexService {
	@Autowired
	private IndexDAO indexDAO;

	@Autowired
	private Util util;

	public List<Map<String, Object>> boardList() {
		return indexDAO.boardList();
	}

	public Map<String, Object> detail(int no) {
		return indexDAO.detail(no);
	}

	public List<Map<String, Object>> freeboard(int cate) {
		return indexDAO.freeboard(cate);
	}

	public int write(Map<String, Object> map) {
		// 여러분 DB에 있는 mid를 넣어주세요.
		map.put("mid", util.getSession().getAttribute("mid"));
		// ip도
		map.put("ip", util.getIP());
		return indexDAO.write(map);
	}

	public List<Map<String, Object>> menu() {
		return indexDAO.menu();
	}

	public void postUpdate(Map<String, Object> map) {
	}

	public List<Map<String, Object>> special(int cate) {
		return indexDAO.special(cate);
	}

	public Object getcateName(int cate) {
		return indexDAO.getcateName(cate);
	}

}
