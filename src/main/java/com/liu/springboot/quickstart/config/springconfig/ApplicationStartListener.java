package com.liu.springboot.quickstart.config.springconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.liu.springboot.quickstart.task.work.SignInSer;


/**
 * 在spring初始化完成后执行
 * @author lgh
 *
 */
@Component
public class ApplicationStartListener implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    private SignInSer signInSer;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // TODO Auto-generated method stub
        System.out.println("=====spring boot 启动成功!!!====");
        signInSer.run();
    }

}
