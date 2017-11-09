package com.liu.springboot.quickstart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

/**
 * 生成js/cssMD5
 * @author xiaoming
 *
 */
@ControllerAdvice
//@CacheConfig(cacheNames = "startMD5")
public class ResourceUrlProviderController {

    @Autowired
    ResourceUrlProvider resourceUrlProvider;

    @Cacheable
    //@ModelAttribute("urls")
    public ResourceUrlProvider urls() {
    	System.out.println("=========生成MD5========");
        return this.resourceUrlProvider;
    }

}