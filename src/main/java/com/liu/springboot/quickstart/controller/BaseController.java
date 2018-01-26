package com.liu.springboot.quickstart.controller;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import com.liu.springboot.quickstart.config.ConstantsConfig;

@Component
public class BaseController {
    
    private static Logger logger = Logger.getLogger(BaseController.class);
    
    @Autowired
    ResourceUrlProvider resourceUrlProvider;

    @ModelAttribute("baseMap")
    public Map<String, Object> getBaseMap(Map<String, Object> map) {
        // 程序目录
        map.put("ap", ConstantsConfig.applicationPath);
        // 资源目录
        map.put("rs", ConstantsConfig.resources+"/");
        return map;
    }
    
    /**
     * 静态资源MD5
     * @return
     */
    @ModelAttribute("urls")
    public ResourceUrlProvider urls() {
        logger.info("-----获得静态资源MD5-----");
        return this.resourceUrlProvider;
    }
}
