package com.liu.springboot.quickstart.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

import com.liu.springboot.quickstart.service.IMonitorService;
import com.liu.springboot.quickstart.service.impl.MonitorServiceImpl;

/**
 * 练习引介增强为借口添加方法
 * 为VideoServiceImpl增加IMonitorService的方法,注通过切面VideoServiceImpl就实现了iMonitorService接口
 * @author lgh
 *
 */
@Aspect
@Component
public class PracticeDeclareParents {

    
    @DeclareParents(value="com.liu.springboot.quickstart.service.impl.VideoServiceImpl",//为VideoServiceImpl增加借口实现
            defaultImpl=MonitorServiceImpl.class)//默认的接口实现类
    public IMonitorService iMonitorService;//要实现的目标接口
}
