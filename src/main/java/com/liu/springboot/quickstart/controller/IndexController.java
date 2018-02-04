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

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liu.springboot.quickstart.bean.DemoObj;
import com.liu.springboot.quickstart.config.ConstantsConfig;
import com.liu.springboot.quickstart.service.IMonitorService;
import com.liu.springboot.quickstart.service.IVideoService;

@Controller
public class IndexController extends BaseController{

	private static Logger logger = Logger.getLogger(IndexController.class);
	
	@Autowired
	private IMonitorService monitorService;
	@Autowired
	private IVideoService videoService;
	
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
	    //使用分页插件
	    //Thread.sleep(30000);
	    //se.invalidate();
	    monitorService.getMonitorInfoBean();
	    videoService.testException2();
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
		try {
			//再一个server中的同一个sql语句执行多次，好像因为sql的缓存导致PageHelper只生效了一次~~~~
			Page page = PageHelper.startPage(2, 2, true);
		    videoService.testException();
		    value.put("total", page.getTotal());
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		value.put("123", "测试json");
		value.put("456", null);
		return value;
	}
}
