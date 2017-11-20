package com.liu.springboot.quickstart.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="connection")
public class ConstantsConfig {
	/**静态文件目录**/
	public static String resources = "";
	/**Reids Task 工作线程数量**/
	public static int redsWorkSize = 5;
	
	/**数据源名称**/
	public static final String DATESOURCE1 = "dataMySQL1";
	public static final String DATESOURCE2 = "dataMySQL2";
	
	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		ConstantsConfig.resources = resources;
	}
	public static int getRedsWorkSize() {
		return redsWorkSize;
	}
	public static void setRedsWorkSize(int redsWorkSize) {
		ConstantsConfig.redsWorkSize = redsWorkSize;
	}	
	
}
