package com.sh.web.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IndexDAO {

	List<Map<String, Object>> boardList();

	Map<String, Object> detail(int no);

	List<Map<String, Object>> freeboard(int cate);

	int write(Map<String, Object> map);

	List<Map<String, Object>> menu();

	Object postUpdate(Map<String, Object> map);

	List<Map<String, Object>> special(int cate);

	Object getcateName(int cate);
	
}
