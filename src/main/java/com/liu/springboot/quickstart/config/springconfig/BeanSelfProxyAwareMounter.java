package com.liu.springboot.quickstart.config.springconfig;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.liu.springboot.quickstart.service.IBeanSelfProxyAware;
import com.liu.springboot.quickstart.util.SpringUtil;

/**
 * 将实现了BeanSelfProxyAware接口的类
 * 将自身代理类注入自身,在spring启动后执行
 * @author lgh
 *
 */
@Component
public class BeanSelfProxyAwareMounter implements ISystemBootAddon {
    private Logger logger = LoggerFactory.getLogger(BeanSelfProxyAwareMounter.class);
    
    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        //启动顺序,越小启动顺序越靠前
        return 0;
    }

    @Override
    public void onReady() {
        // TODO Auto-generated method stub
        //从容器中获得所有注入的自动代理的bean
        Map<String, IBeanSelfProxyAware> proxyAwareMap = SpringUtil.getBeans(IBeanSelfProxyAware.class);
        if(proxyAwareMap != null) {
            for(IBeanSelfProxyAware beanSelfProxyAware : proxyAwareMap.values()) {
                beanSelfProxyAware.setSelfProxy(beanSelfProxyAware);
                if(logger.isDebugEnabled()) {
                    logger.debug("{}注册自身被代理的事例");
                }
            }
        }
    }

}
