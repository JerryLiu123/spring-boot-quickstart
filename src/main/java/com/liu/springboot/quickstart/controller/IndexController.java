package com.liu.springboot.quickstart.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.liu.springboot.quickstart.bean.DemoObj;
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
	
	@RequestMapping(value="/async/mode/{id}")
	public WebAsyncTask<ModelAndView> asyncCon(@PathVariable(value="id")String id, Map<String, Object> dataMap) {
	    System.out.println("/async/mode/"+id+"被调用 thread id is : " + Thread.currentThread().getId());
	    Callable<ModelAndView> callable = new Callable<ModelAndView>() {
	        public ModelAndView call() throws Exception {
	            Thread.sleep(3000); //假设是一些长时间任务
	            ModelAndView mav = new ModelAndView("index");
	            mav.addObject("result", "执行成功");
	            mav.addObject("id", id);
	            System.out.println("执行成功 thread id is : " + Thread.currentThread().getName());
	            return mav;
	        }
	    };
	    
	    return new WebAsyncTask<ModelAndView>(5000, callable);	    
	}
	
	
	@ResponseBody
    @RequestMapping(value="/async/json/{id}")
    public WebAsyncTask<Map<String, Object>> asyncJson(@PathVariable(value="id")String id) {
	    Map<String, Object> value = new HashMap<String, Object>();
	    logger.info("/async/json/"+id+"被调用 thread id is : " + Thread.currentThread().getId());
        Callable<Map<String, Object>> callable = new Callable<Map<String, Object>>() {
            public Map<String, Object> call() throws Exception {
                Thread.sleep(7000); //假设是一些长时间任务
                value.put("123", "测试json");
                value.put("456", null);
                value.put("789", id);
                logger.info("执行成功 thread id is : " + Thread.currentThread().getId());
                return value;
            }
        };
        
        WebAsyncTask<Map<String, Object>> asyncTask = new WebAsyncTask<Map<String, Object>>(5000, callable);
        asyncTask.onTimeout(new Callable<Map<String, Object>>() {

            @Override
            public Map<String, Object> call() throws Exception {
                // TODO Auto-generated method stub
                value.put("error", "执行超时");
                logger.info("执行超时thread id is : " + Thread.currentThread().getId());
                return value;
            }
            
        });
        return asyncTask;      
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
		    Thread.sleep(10000); 
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
