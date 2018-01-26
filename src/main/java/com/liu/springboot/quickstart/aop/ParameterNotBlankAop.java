package com.liu.springboot.quickstart.aop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Aspect
@Component//将bean 实例化到spring中
public class ParameterNotBlankAop implements Ordered{
	
	
    
    /**
     * @annotation(M)：当方法上有注解@M时匹配
     * @args(M):当方法的参数上有@M注解的类或子类时匹配
     * @within(M):匹配标注了@M的类和子类的所有方法
     * @target(M):匹配标注了@M的类的所有方法
     */
	@Pointcut("@annotation(com.liu.springboot.quickstart.aop.annotation.ParameterNotBlank)")
	public void parameterNotBlank(){}
	
	@Before("parameterNotBlank()")
	public void beforeExec(JoinPoint joinPoint){
		System.out.println("======执行方法为:"+joinPoint.getSignature().getDeclaringTypeName()+"." + joinPoint.getSignature().getName()+"======");
	}
	
	@Around("parameterNotBlank()")
	public Object aroundExec(ProceedingJoinPoint pjp) throws Throwable{
		//获得目标方法的参数
		Object[] args = pjp.getArgs();
		if(args != null && args.length > 0) {
			for(Object a : args) {
				if(!notNull(a)) {
					throw new Throwable("执行方法："+pjp.getSignature().getDeclaringTypeName()+"."+pjp.getSignature().getName()+
							">>>>方法参数不可为空!!!!");
				}
			}
		}
		return pjp.proceed();
	}
	
	/**
	 * 如果value 为空或没有数据则返回false
	 * @author lgh
	 * @param value
	 * @return
	 */
	private boolean notNull(Object value) {
		if(value == null) {
			return false;
		}else {
			if(value instanceof String ||
					value instanceof Integer ||
					value instanceof Long ||
					value instanceof Double ||
					value instanceof Float) {
				return !StringUtils.isEmpty(String.valueOf(value));
			}else if(value instanceof List) {
				return value != null && !((List)value).isEmpty();
			}else if(value instanceof Set) {
				return value != null && !((Set)value).isEmpty();
			}else if(value instanceof Map) {
				return value != null && !((Map)value).isEmpty();
			}else {
				return true;
			}
		}
	}

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return 2;
    }
}
