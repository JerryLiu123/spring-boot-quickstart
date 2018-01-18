package com.liu.springboot.quickstart;

import org.apache.log4j.Logger;
import org.apache.tomcat.jni.Thread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.liu.springboot.quickstart.task.work.SignInSer;
import com.liu.springboot.quickstart.util.SpringUtil;

/**
 * soring boot 启动的主类~
 * 新建项目的时候就不要用 web项目了~老老实实用java项目吧~ 
 * java -jar -Dspring.config.location=XXX.yml -Dlogpath=XXX -Dservicename=ser1 XXX.jar --spring.profiles.active=XXX
 * @author xiaoming
 *
 */
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.liu.springboot.quickstart"})
@SpringBootApplication
@ServletComponentScan //扫描监听器注解
public class Application extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {

	private static Logger logger = Logger.getLogger(Application.class);
	@Override
	public void customize(ConfigurableEmbeddedServletContainer arg0) {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(Application.class);  
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(Application.class, args);
		//logger.info("=====spring boot 启动成功!!!====");
	}

}
