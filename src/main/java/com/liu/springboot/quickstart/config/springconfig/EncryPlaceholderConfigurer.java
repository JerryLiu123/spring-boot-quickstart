package com.liu.springboot.quickstart.config.springconfig;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.liu.springboot.quickstart.util.DefaultEncryptor;

/**
 * yml 属性加密
 * @author lgh
 *
 */
@Configuration
public class EncryPlaceholderConfigurer {

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor() {
        return new DefaultEncryptor();
    }
}
