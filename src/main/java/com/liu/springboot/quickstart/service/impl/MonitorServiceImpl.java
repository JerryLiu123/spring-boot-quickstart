package com.liu.springboot.quickstart.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.liu.springboot.quickstart.bean.MonitorInfoBean;
import com.liu.springboot.quickstart.service.IMonitorService;

@Service(value="monitorService")
public class MonitorServiceImpl implements IMonitorService {

    @Cacheable(value="monitorInfo")
    @Override
    public MonitorInfoBean getMonitorInfoBean() throws Exception {
        // TODO Auto-generated method stub
        System.err.println("------获得ser服务器性能数据-----");
        MonitorInfoBean bean = new MonitorInfoBean();
        bean.setCpuRatio(130);
        return bean;
    }

}
