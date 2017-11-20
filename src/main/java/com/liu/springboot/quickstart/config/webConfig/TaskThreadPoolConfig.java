package com.liu.springboot.quickstart.config.webConfig;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.liu.springboot.quickstart.config.ConstantsConfig;

@Configuration
@ConfigurationProperties(prefix="spring.task")
public class TaskThreadPoolConfig implements AsyncConfigurer{

	private static Logger logger = Logger.getLogger(TaskThreadPoolConfig.class);
	
    private int corePoolSize;    //线程池维护线程的最少数量
    private int maxPoolSize;     //线程池维护线程的最大数量
    private int keepAliveSeconds;//允许的空闲时间  
    private int queueCapacity;   //缓存队列
    private String threadNamePrefix;//线程前缀
    
    @Bean(name="taskPool")
	@Override
	public Executor getAsyncExecutor() {
		// TODO Auto-generated method stub
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
        //如果线程的总数量比队列数量还小的话则将队列总数量设置为队列数量
        if(maxPoolSize < ConstantsConfig.redsWorkSize) {
        	logger.warn("线程池大小过低!!!现将线程池大小设置为redis工作线程的数量+5");
        	executor.setCorePoolSize(ConstantsConfig.redsWorkSize + 1);
        	executor.setMaxPoolSize(ConstantsConfig.redsWorkSize + 5);
        }else {
            executor.setCorePoolSize(corePoolSize);
        	executor.setMaxPoolSize(maxPoolSize);
        }
        executor.setQueueCapacity(queueCapacity);    
        executor.setKeepAliveSeconds(keepAliveSeconds);    
        executor.setThreadNamePrefix(threadNamePrefix);    
    
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务    
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行    
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());  
        executor.initialize();    
        return executor; 
	}
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		// TODO Auto-generated method stub
		return new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable arg0, Method arg1, Object... arg2) {
				logger.error("=========================="+arg0.getMessage()+"=======================", arg0);
				logger.error("exception method:"+arg1.getName());  
			}  
		};
	}    
    
	public int getCorePoolSize() {
		return corePoolSize;
	}
	public void setCorePoolSize(int corePoolSize) {
		this.corePoolSize = corePoolSize;
	}
	public int getMaxPoolSize() {
		return maxPoolSize;
	}
	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}
	public int getKeepAliveSeconds() {
		return keepAliveSeconds;
	}
	public void setKeepAliveSeconds(int keepAliveSeconds) {
		this.keepAliveSeconds = keepAliveSeconds;
	}
	public int getQueueCapacity() {
		return queueCapacity;
	}
	public void setQueueCapacity(int queueCapacity) {
		this.queueCapacity = queueCapacity;
	}
	public String getThreadNamePrefix() {
		return threadNamePrefix;
	}
	public void setThreadNamePrefix(String threadNamePrefix) {
		this.threadNamePrefix = threadNamePrefix;
	}
	
}
