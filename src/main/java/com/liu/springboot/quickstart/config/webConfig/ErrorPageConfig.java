package com.liu.springboot.quickstart.config.webConfig;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 异常处理配置
 * @author xiaoming
 *
 */
@Configuration
public class ErrorPageConfig implements EmbeddedServletContainerCustomizer {

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		// TODO Auto-generated method stub
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/er/404"));
		container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/er/401"));
		container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/er/500"));
	}

}
