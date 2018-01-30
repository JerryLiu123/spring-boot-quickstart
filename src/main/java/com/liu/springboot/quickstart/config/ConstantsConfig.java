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
	/**应用路径**/
	public static String applicationPath;
	/**节点编号**/
	public static Long serID = 2L;
	/**zookeeper 链接**/
	public static String zkHosts = "";
	/**zookeeper 超时session时间**/
	public static String sessionTimeOut = "10000";
	/**DES加密Key**/
	public static String desKey = "mykey";
	
	/**数据源名称**/
	//public static final String DATESOURCE1 = "dataMySQL1";
	//public static final String DATESOURCE2 = "dataMySQL2";
	
	
	/**zookeeper 应用根目录**/
	public static final String ZKMAINZONE="/myapp";
	/**zookeeper 每一个ser注册的节点名目录**/
	public static final String ZKSERNODE = ConstantsConfig.ZKMAINZONE + "/ser";
	/**zookeeper 执行任务目录**/
	public static final String ZKWORKNODE = ConstantsConfig.ZKMAINZONE + "/work";
	/**zookeeper 所有的任务目录**/
    public static final String ZKWORKALLNODE = ConstantsConfig.ZKWORKNODE + "/all";
	
	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		ConstantsConfig.resources = resources;
	}
	public int getRedsWorkSize() {
		return redsWorkSize;
	}
	public void setRedsWorkSize(int redsWorkSize) {
		ConstantsConfig.redsWorkSize = redsWorkSize;
	}
    public String getApplicationPath() {
        return applicationPath;
    }
    public void setApplicationPath(String applicationPath) {
        ConstantsConfig.applicationPath = applicationPath;
    }
    public String getZkHosts() {
        return zkHosts;
    }
    public void setZkHosts(String zkHosts) {
        ConstantsConfig.zkHosts = zkHosts;
    }
    public String getSessionTimeOut() {
        return sessionTimeOut;
    }
    public void setSessionTimeOut(String sessionTimeOut) {
        ConstantsConfig.sessionTimeOut = sessionTimeOut;
    }
    public Long getSerID() {
        return serID;
    }
    public void setSerID(Long serID) {
        ConstantsConfig.serID = serID;
    }
    public static void setDesKey(String desKey) {
        ConstantsConfig.desKey = desKey;
    }	
	
}
