package com.liu.springboot.quickstart.config.webconfig;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;


@WebListener
@Component
public class MyHttpSessionListener implements HttpSessionListener {

    private static Logger logger = Logger.getLogger(MyHttpSessionListener.class);
    
    @Override
    public void sessionCreated(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
        logger.info("-----session创建-----"+arg0.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent arg0) {
        // TODO Auto-generated method stub
        logger.info("-----session销毁-----"+arg0.getSession().getId());
    }

}
