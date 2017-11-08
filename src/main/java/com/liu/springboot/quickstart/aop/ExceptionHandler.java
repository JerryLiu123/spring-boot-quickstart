package com.liu.springboot.quickstart.aop;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.context.annotation.Configuration;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
@Configuration
public class ExceptionHandler implements ThrowsAdvice {

	private static Logger LOGGER =  Logger.getLogger(ExceptionHandler.class);
	/**
	 * 异常处理 aop
	 * @param throwable 产生的异常
	 */
	@AfterThrowing(value="excudeService()", throwing="e")
	@SuppressWarnings("unused")
	public void afterThrowing(Exception e) throws Throwable{
		// TODO Auto-generated method stub
		LOGGER.info("--------集中异常处理>开始---------");
		LOGGER.error("抛出的异常:    " + e.getMessage());  
		LOGGER.error("异常原因:     " + e.getCause());
		LOGGER.error("堆栈痕迹长度:   " + e.getStackTrace().length);
		LOGGER.error(e);
		LOGGER.info("--------集中异常处理>结束---------");
	}
	
	@Pointcut("execution(* com.liu.springboot.quickstart.service..*.*(..))")
	private void excudeService() {
		// TODO Auto-generated method stub

	}
}
