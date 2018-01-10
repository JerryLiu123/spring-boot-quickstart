package com.liu.springboot.quickstart.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 处理 404,500等错误异常
 * @author xiaoming
 *
 */
@Controller
public class DefErrorController extends BaseController{

	private static Logger logger = Logger.getLogger(DefErrorController.class);
	
	/**
	 * 捕获500,404等异常
	 * @param req
	 * @param resp
	 * @param dataMap
	 * @param errorCode
	 * @return
	 */
	//@ExceptionHandler(value=Exception.class)
	@RequestMapping(value="/er/{errorCode}")
	public String defaultErrorHandler(HttpServletRequest req,
			HttpServletResponse resp, 
			Map<String, Object> dataMap,
			@PathVariable("errorCode") String errorCode) {
		logger.info(errorCode+">>>>>>>>>>>异常拦截");
		dataMap.put("errorCode", errorCode);
		dataMap.put("url", req.getRequestURL());
		if(!StringUtils.isEmpty(req.getParameter("ex"))) {
		    dataMap.put("ex", req.getParameter("ex"));
		}
		return "error/all";
	}
}
