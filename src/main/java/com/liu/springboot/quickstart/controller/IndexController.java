package com.liu.springboot.quickstart.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.liu.springboot.quickstart.bean.DemoObj;
import com.liu.springboot.quickstart.config.ConstantsConfig;
import com.liu.springboot.quickstart.service.IMonitorService;

@Controller
public class IndexController extends BaseController{

	private static Logger logger = Logger.getLogger(IndexController.class);
	
	@Autowired
	private IMonitorService monitorService;
	
	@RequestMapping(value="/index")
	public String index(HttpServletRequest req,
			HttpServletResponse resp, Map<String, Object> dataMap) throws Exception {
		//throw new Exception("测试异常全局拦截");
	    if(req.getSession().getAttribute("key") == null) {
	        logger.info("session中信息为空");
	        req.getSession().setAttribute("key", "value");
	    }else {
	        logger.info(req.getSession().getAttribute("key"));
	    }
	    
	    //se.invalidate();
	    monitorService.getMonitorInfoBean();
	    DemoObj demoObj = new DemoObj(333l, "WYF");
	    dataMap.put("demoObj", demoObj);
		return "index";
	}
	
	@RequestMapping(value="toSocket", method = RequestMethod.GET)
	public String toSocket() {
	    return "socket";
	}
	
	@ResponseBody
	@RequestMapping(value="/json")
	public Map<String, Object> getJson(){
		Map<String, Object> value = new HashMap<String, Object>();
		value.put("123", "测试json");
		value.put("456", null);
		return value;
	}
}
