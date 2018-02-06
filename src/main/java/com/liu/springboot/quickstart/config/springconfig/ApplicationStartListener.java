package com.liu.springboot.quickstart.config.springconfig;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.OrderComparator;
import org.springframework.stereotype.Component;

import com.liu.springboot.quickstart.config.springconfig.bootaddon.ISystemBootAddon;


/**
 * 在spring初始化完成后执行
 * @author lgh
 *
 */
@Component
public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent>{

    private Logger logger = LoggerFactory.getLogger(ApplicationStartListener.class);
    private List<ISystemBootAddon> systemBootAddons = Collections.EMPTY_LIST;
    private boolean hasRunOnce = false;
    
    //注入所有实现了systemBootAddon接口的插件
    //不是必须的
    @Autowired(required = false)
    public void setSystemBootAddons(List<ISystemBootAddon> systemBootAddons) {
        //Assert.notEmpty(systemBootAddons, "");
        //当类继承Ordered后可利用下载方法进行排序，顺序是getOrder获得的数值的大小
        OrderComparator.sort(systemBootAddons);
        this.systemBootAddons = systemBootAddons;
    }
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // TODO Auto-generated method stub
        System.out.println("=====spring boot 启动成功!!!====");
        if(!hasRunOnce) {
            for(ISystemBootAddon a : systemBootAddons) {
                a.onReady();//执行插件
                logger.info("执行插件:{}", a.getClass().getCanonicalName());
            }
            hasRunOnce = true;
        }else {
            if(logger.isDebugEnabled()) {
                logger.debug("已经执行过容器启动插件，不在执行");
            }
        }
    }

}
