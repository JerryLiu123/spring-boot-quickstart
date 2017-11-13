package com.liu.springboot.quickstart.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.liu.springboot.quickstart.config.ConstantsConfig;

/**
 * 处理 404,500等错误异常
 * @author xiaoming
 *
 */
@Controller
public class DefErrorController {

	private static Logger logger = Logger.getLogger(DefErrorController.class);
	
	//@ExceptionHandler(value=Exception.class)
	@RequestMapping(value="/error/{errorCode}")
	public String defaultErrorHandler(HttpServletRequest req,
			HttpServletResponse resp, 
			Map<String, Object> dataMap,
			@PathVariable("errorCode") String errorCode) {
		logger.info(errorCode+">>>>>>>>>>>异常拦截");
		dataMap.put("rs", ConstantsConfig.resources+"/");
		dataMap.put("errorCode", errorCode);
		dataMap.put("url", req.getRequestURL());
		return "error/all";
	}
}
