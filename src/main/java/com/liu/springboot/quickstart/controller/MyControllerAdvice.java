package com.liu.springboot.quickstart.controller;



import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 当controller抛出异常时的处理
 * @author lgh
 *
 */
@ControllerAdvice
public class MyControllerAdvice {

	private static Logger logger = Logger.getLogger(MyControllerAdvice.class);
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
     * 全局异常捕捉处理(捕获Controller中抛出的异常)
     * @param ex
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public String errorHandler(Exception ex, 
			RedirectAttributes attr) {
    	logger.error("-----捕获到Controller中异常-----", ex);
    	attr.addFlashAttribute("ex", ex.getMessage());
	    return "redirect:er/500";
    }
}
