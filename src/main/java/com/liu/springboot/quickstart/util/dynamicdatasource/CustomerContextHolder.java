package com.liu.springboot.quickstart.util.dynamicdatasource;

/** 
 * <b>function:</b> 多数据源 
 * @author hoojo 
 * @createDate 2013-9-27 上午11:36:57 
 * @file CustomerContextHolder.java 
 * @package com.hoo.framework.spring.support 
 * @project SHMB 
 * @blog http://blog.csdn.net/IBM_hoojo 
 * @email hoojo_@126.com 
 * @version 1.0 
 */  
public abstract class CustomerContextHolder {  
       
    private static final ThreadLocal<DBEnum> contextHolder = new ThreadLocal<DBEnum>();    
       
    public static void setContextType(DBEnum contextType) {    
        contextHolder.set(contextType);    
    }    
         
    public static DBEnum getContextType() {    
        return contextHolder.get();    
    }    
         
    public static void clearContextType() {    
        contextHolder.remove();    
    }    
}
