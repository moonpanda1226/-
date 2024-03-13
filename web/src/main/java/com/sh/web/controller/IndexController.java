package com.sh.web.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sh.web.service.IndexService;
import com.sh.web.util.Util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

	@Autowired
	private Util util;

	@Autowired
	private IndexService indexService;

	@GetMapping({ "/index", "/" })
	public String index(Model model) {
		// 메뉴 불러오기
		List<Map<String, Object>> menu = indexService.menu();
		model.addAttribute("menu", menu);

		return "index";
	}

	@GetMapping("/freeboard")
	public String freeboard(@RequestParam(value = "cate", defaultValue = "1") int cate, Model model) {
		// 메뉴 불러오기
		List<Map<String, Object>> menu = indexService.menu();
		model.addAttribute("menu", menu);

		List<Map<String, Object>> board = indexService.freeboard(cate);
		model.addAttribute("board", board);
		model.addAttribute("cate", cate);
		model.addAttribute("catename", indexService.getcateName(cate));
		return "board";
	}

	@GetMapping("/special")
	public String special(@RequestParam(value = "cate", defaultValue = "7") int cate, Model model) {
		
		System.out.println(cate);
		// 메뉴 불러오기

		if (util.getSession().getAttribute("mid") == null) {
			return "redirect:/login";
		} else {

			if (util.str2Int(util.getSession().getAttribute("mgrade")) > 5) {
				List<Map<String, Object>> menu = indexService.menu();
				model.addAttribute("menu", menu);
				
				List<Map<String, Object>> special = indexService.special(cate);
				model.addAttribute("special", special);
				

				System.out.println(util.str2Int(util.getSession().getAttribute("mgrade")) > 5);
				return "special";
			} else {
				return "redirect:/freeboard";
			}
		}


	}



	// 2024-03-07 일단은 안드로이드 앱 프로그래밍
	// 상세보기 -> no값을 넘기니까 잡아야죠 -> 오나 확인하기
	// detail.html
	// 값 -> DB에 물어보기 (어떤 형태로 오는지?) -> BoardDTO에 담아진 형태 -> 그럼 붙여주기

	@GetMapping("/detail")
	public String detail(@RequestParam("no") int no, Model model) {
		//메뉴 불러오기
		List<Map<String, Object>> menu = indexService.menu();
		model.addAttribute("menu", menu);
		
		Map<String, Object> detail = indexService.detail(no); 
		model.addAttribute("detail", detail);
		return "detail";
	}

	@GetMapping("/write")
	public String write() {
		return "write";
	}

	@PostMapping("/write")
	public String write(@RequestParam Map<String,Object> map) {	
		//로그인 검사해주세요
		int result = indexService.write(map);
		//System.out.println(result);
		String url = "freeboard";
		return "redirect:/" + url;
	}

	@PostMapping("/postDel")
	public String postDel(@RequestParam("no") int no) {
		System.out.println(no);
		return "redirect:/freeboard";
	}

	@PostMapping("/postUpdate")
	public String postUpdate(@RequestParam() Map<String, Object> map) {
		indexService.postUpdate(map);
		return "redirect:/update";
	}
	//2024-03-12
	@GetMapping("/fileUp")
	public String fileUp() {
		return "fileUp";
	}
	
	@PostMapping("/fileUp")
	public String fileUp(@RequestParam("fileUp") MultipartFile file) {
		System.out.println(file.getName());
		System.out.println(file.getSize());
		System.out.println(file.getOriginalFilename());
		
		//String url = util.req().getServletContext().getRealPath("/upload");
		File url = new File(util.req().getServletContext().getRealPath("/upload"));
		url.mkdirs();
		//UUID를 붙이던지
		//년월일시분초
		//나노초
		//파일 업로드시 UUID+ 실제 파일명.확장자
		//파일 다운로드시 원래 파일명.확장자 -------------------
		
		
		//UUID
		UUID uuid = UUID.randomUUID();
		System.out.println("원본 파일 명 : " + file.getOriginalFilename());
		System.out.println("UUID 파일 명 : " + uuid.toString() + file.getOriginalFilename());
		
		//날짜를 뽑아서 파일명 변경하기
		LocalDateTime ldt = LocalDateTime.now();
		String ldtFormat = ldt.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss"));
		System.out.println("날짜 파일명 : " + ldtFormat + file.getOriginalFilename());
		
		File upFileName = new File(url, ldtFormat + file.getOriginalFilename());
	
			try {
				file.transferTo(upFileName);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		
		System.out.println("실제 경로:" +url);
				
		
		return "redirect:/fileUp";
	}
	
	//downfile@파일명
	@ResponseBody
	@GetMapping("/downfile@{file}") //path variable
	public void down(@PathVariable("file") String file, HttpServletRequest request, HttpServletResponse response){
		System.out.println("경로에 들어온 파일명 : " + file);

		String url = "/static/img/";
		//File url = new File(util.req().getServletContext().getRealPath("/upload"));
		File serverFile = new File(url, file);
		
		try {
			byte[] fileByte = FileCopyUtils.copyToByteArray(serverFile);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment; fileName\"" + URLEncoder.encode(url+file, "UTF-8")+"\";");
			response.setHeader("Content-Transfer-Encoding","binary");
			response.getOutputStream().write(fileByte);
			response.getOutputStream().flush();
			response.getOutputStream().close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
}
