package com.liu.springboot.quickstart.service;

import com.liu.springboot.quickstart.bean.MonitorInfoBean;

/**
 * 获得服务器性能
 * @author lgh
 *
 */
public interface IMonitorService {
    /**
     * 获得当前服务器性能
     * @return
     * @throws Exception
     */
    public MonitorInfoBean getMonitorInfoBean() throws Exception; 
    
    public MonitorInfoBean getMonitorInfoBean2() throws Exception; 
}
