package com.sh.web.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sh.web.util.Sensitive;
import com.sh.web.util.Util;

@Controller
public class APIController {
	
	@Autowired
	private Sensitive sensitive;

	@GetMapping("/airKoreaXML")
	public String airKoreaXML(Model model) throws IOException, ParserConfigurationException, SAXException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustFrcstDspth");
		urlBuilder.append("?serviceKey="+sensitive.getServiceKey());
		urlBuilder.append("&returnType=xml");
		urlBuilder.append("&numOfRows=100");
		urlBuilder.append("&pageNo=1");
		urlBuilder.append("&searchDate=2024-03-12");
		urlBuilder.append("&InformCode=PM10");
		
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("응답 결과 : " + conn.getResponseCode()); //응답 결과 : 200
		
	
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(conn.getInputStream());
		document.getDocumentElement().normalize();
		
		System.out.println(document.getDocumentElement().getNodeName());
		
	
		
				
				
		return "airkorea"; // 반드시 URL과 같을 필요가 없습니다.
	}
	
	
	@GetMapping("/airKorea")
	public String airKorea(Model model) throws IOException, ParseException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustFrcstDspth");
		urlBuilder.append("?serviceKey="+sensitive.getServiceKey());
		urlBuilder.append("&returnType=json");
		urlBuilder.append("&numOfRows=100");
		urlBuilder.append("&pageNo=1");
		urlBuilder.append("&searchDate=2024-03-12");
		urlBuilder.append("&InformCode=PM10");
		
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-type", "application/json");
		System.out.println("응답 결과 : " + conn.getResponseCode()); //응답 결과 : 200
		
		//data.go.kr에서 에어코리아 접속해서 데이터 불러오기
		
		// json -> 자바 데이터 타입형태로 변경 -> 출력
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(url.openStream()));
		//System.out.println(jsonObject.toString());
		System.out.println(jsonObject.toJSONString());
		
		Map<String, Object> map = (Map<String, Object>) jsonObject.get("response");
		System.out.println("response ::::::::::::: " + map);
		map = (Map<String, Object>) map.get("body");
		System.out.println("body ::::::::::::: " + map);
		JSONArray jsonArray = (JSONArray) map.get("items");
		System.out.println("items :::::::::: " + jsonArray);
		
		model.addAttribute("data", jsonArray);
		
		return "airkorea"; // 반드시 URL과 같을 필요가 없습니다.
	}
}
