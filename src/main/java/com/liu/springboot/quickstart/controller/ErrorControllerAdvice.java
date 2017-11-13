package com.liu.springboot.quickstart.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.liu.springboot.quickstart.config.ConstantsConfig;

/**
 * 当controller抛出异常时的处理
 * @author lgh
 *
 */
@ControllerAdvice
public class ErrorControllerAdvice {

	private static Logger logger = Logger.getLogger(ErrorControllerAdvice.class);
//    /**
//     * 应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器
//     * @param binder
//     */
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//    	logger.info("============应用到所有@RequestMapping注解方法，在其执行之前初始化数据绑定器");
//    }

//    /**
//     * 把值绑定到Model中，使全局@RequestMapping可以获取到该值
//     * @param model
//     */
//    @ModelAttribute
//    public Map<String, Object> addAttributes(Map<String, Object> model) {
//    	logger.info("============应用到所有@RequestMapping注解方法，在其执行之前把返回值放入Model");
//    	model.put("rs", ConstantsConfig.resources+"/");
//    	return model;
//    }

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ModelAndView errorHandler(HttpServletRequest req,
			HttpServletResponse resp,
			Exception ex) {
    	logger.info("集中异常处理!!!");
	    ModelAndView modelAndView = new ModelAndView();
	    modelAndView.setViewName("error/all");
	    modelAndView.addObject("errorCode", "500");
	    modelAndView.addObject("ex", ex.getMessage());
	    modelAndView.addObject("rs", ConstantsConfig.resources+"/");
	    return modelAndView;
    }
	
}
