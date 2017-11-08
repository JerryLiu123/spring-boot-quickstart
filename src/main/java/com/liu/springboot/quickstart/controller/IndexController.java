package com.liu.springboot.quickstart.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.springboot.quickstart.config.ConstantsConfig;

@Controller
public class IndexController {

	private static Logger logger = Logger.getLogger(IndexController.class);
	
	@RequestMapping(value="/index")
	public String index(HttpServletRequest req,
			HttpServletResponse resp, 
			Map<String, Object> dataMap) {
		dataMap.put("rs", ConstantsConfig.resources+"/");
		return "index";
	}
	
	@ResponseBody
	@RequestMapping(value="/json")
	public Map<String, Object> getJson(){
		Map<String, Object> value = new HashMap<String, Object>();
		value.put("123", 1243);
		return value;
	}
}