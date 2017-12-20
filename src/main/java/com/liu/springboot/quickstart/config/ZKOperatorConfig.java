package com.liu.springboot.quickstart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.liu.springboot.quickstart.util.ZookeeperOperator;

@Component
public class ZKOperatorConfig {

    @Bean(name="zookeeperOperator")
    public ZookeeperOperator zookeeperOperator() {
        ZookeeperOperator op = new ZookeeperOperator(ConstantsConfig.zkHosts);
        op.init();
        return op;
    }
}
