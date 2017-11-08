package com.liu.springboot.quickstart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="connection")
public class ConstantsConfig {

	/**数据源名称**/
	public static final String DATESOURCE1 = "dataMySQL1";
	public static final String DATESOURCE2 = "dataMySQL2";	
	
}
