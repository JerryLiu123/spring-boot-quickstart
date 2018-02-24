package com.liu.springboot.quickstart.config.springconfig.bootaddon.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.liu.springboot.quickstart.config.springconfig.bootaddon.ISystemBootAddon;
import com.liu.springboot.quickstart.task.work.SignInSer;

/**
 * 注册自身的启动插件
 * @author lgh
 *
 */
@Component
public class SignInMounter implements ISystemBootAddon {

    @Autowired
    private SignInSer signInSer;
    
    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return 5;
    }

    @Override
    public void onReady() {
        // TODO Auto-generated method stub
        //signInSer.run();
    }

}
