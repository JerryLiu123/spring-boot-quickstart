package com.liu.springboot.quickstart.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.liu.springboot.quickstart.bean.MonitorInfoBean;
import com.liu.springboot.quickstart.service.IBeanSelfProxyAware;
import com.liu.springboot.quickstart.service.IMonitorService;

@Service(value="monitorService")
public class MonitorServiceImpl implements IMonitorService, IBeanSelfProxyAware {

    @Override
    public MonitorInfoBean getMonitorInfoBean() throws Exception {
        // TODO Auto-generated method stub
//        System.err.println("------获得ser服务器性能数据-----");
//        MonitorInfoBean bean = new MonitorInfoBean();
//        bean.setCpuRatio(130);
        //这样的内部方法调用是无法使用切面的，因为jvm会直接调用未被增强的原始类
        //MonitorInfoBean bean = getMonitorInfoBean2();
        //这样可以被增强，因为使用了monitorServiceImpl的代理类
        MonitorInfoBean bean = monitorServiceImpl.getMonitorInfoBean2();
        return bean;
    }

    @Cacheable(cacheManager="redisCacheManager", cacheNames="monitorInfo")
    @Override
    public MonitorInfoBean getMonitorInfoBean2() throws Exception {
        // TODO Auto-generated method stub
        System.err.println("------获得ser服务器性能数据-----");
        MonitorInfoBean bean = new MonitorInfoBean();
        bean.setCpuRatio(130);
        return bean;
    }

    //这样注入主要是为了让方法在类中内部调用的时候也能使用AOP
    private MonitorServiceImpl monitorServiceImpl;
    @Override
    public void setSelfProxy(Object object) {
        // TODO Auto-generated method stub
        System.err.println(object.getClass().getCanonicalName());
        monitorServiceImpl = (MonitorServiceImpl) object;
    }

}
