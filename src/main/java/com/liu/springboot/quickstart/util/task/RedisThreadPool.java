package com.liu.springboot.quickstart.util.task;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 初始化redis任务队列
 * @author lgh
 *
 */
@Component
public class RedisThreadPool implements InitializingBean{
	private static Logger logger = Logger.getLogger(RedisThreadPool.class);

	@Autowired
    private @Qualifier("redisTemplate")RedisTemplate redisTemplateTask;  
    private String key = "springbootquickstart";  
    //private int cap = Short.MAX_VALUE;//最大阻塞的容量，超过容量将会导致清空旧数据  
    private byte[] rawKey;  
    private RedisConnectionFactory factory;  
    private RedisConnection connection;//for blocking  
    private BoundListOperations<String, Task> listOperations;//noblocking
    //感觉hash主要用于对于对象的修改时比较有用，但是在本实例中对象并不需要修改，所以就没有使用hash
    //private BoundHashOperations<String, byte[], Task> hashOperations;
      
    private boolean isClosed;  
    /* 默认池中线程数 */
    private int worker_num = 5;
    /* 池中的所有线程 */
    public PoolWorker[] workers;
      
    /**
     * 初始化方法
     * @throws Exception
     */
	@SuppressWarnings("unchecked")
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		logger.info("初始化"+worker_num+"个线程池");
        factory = redisTemplateTask.getConnectionFactory();  
        connection = RedisConnectionUtils.getConnection(factory);  
        rawKey = redisTemplateTask.getKeySerializer().serialize(key);  
        listOperations = redisTemplateTask.boundListOps(key); 
        //hashOperations = redisTemplate.boundHashOps(key);
        workers = new PoolWorker[worker_num];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new PoolWorker(i);
        }
	}
    /**
    * 线程池信息
    * @return
    */
    public String getInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("\nTask listOperations Size:" + listOperations.size());
        for (int i = 0; i < workers.length; i++) {
            sb.append("\nWorker " + i + " is "
                    + ((workers[i].isWaiting()) ? "Waiting." : "Running."));
        }
        return sb.toString();
    }

    /** 
     * 从队列的头，插入 
     * @throws Exception 
     */  
    public void pushFromHead(Task value) throws Exception{ 
        listOperations.leftPush(value);  
        //listOperations.notifyAll();
    }  
    /**
     * 从尾部插入  
     * @param value
     * @throws Exception 
     */
    public void pushFromTail(Task value) throws Exception{ 
        listOperations.rightPush(value);  
        //listOperations.notifyAll();
    }  
      
    /** 
     * 从头取出任务
     * @return null if no item in queue 
     */  
    public Task takeFromHead() throws Exception{  
        return listOperations.leftPop();  
    }  
    
    /**
     * 从尾部取出任务
     * @return
     */
    public Task takeFromTail() throws Exception{  
        return listOperations.rightPop();  
    }  
      
    /**
    * 销毁线程池
    */
    public synchronized void destroy() {
    	if(isClosed){
            for (int i = 0; i < worker_num; i++) {
                workers[i].stopWorker();
                workers[i] = null;
            }
            RedisConnectionUtils.releaseConnection(connection, factory); 
    	}
    }

    /**
    * 池中工作线程
    * 
    * @author lgh
    */
    private class PoolWorker extends Thread {
        private int index = -1;
        /* 该工作线程是否有效 */
        private boolean isRunning = true;
        /* 该工作线程是否可以执行新任务 */
        private boolean isWaiting = true;

        public PoolWorker(int index) {
            this.index = index;
            start();
        }

        public void stopWorker() {
            this.isRunning = false;
        }

        public boolean isWaiting() {
            return this.isWaiting;
        }
        /**
        * 循环执行任务
        */
        public void run() {
            while (isRunning) {
                Task r = null;
                synchronized (listOperations) {
                    while (listOperations.size()<=0) {
                        try {
                            /* 任务队列为空，则等待有新任务加入从而被唤醒 */
                        	listOperations.wait(200);
                        } catch (InterruptedException ie) {
                            logger.error(ie);
                        }
                    }
                    /* 取出任务执行 */
                    try {
                    	//反序列话对象
//                    	ByteArrayInputStream in = new ByteArrayInputStream(takeFromHead());
//                    	if(in != null) {
//                         	ObjectInputStream sIn = new ObjectInputStream(in);
//                        	Object ob = sIn.readObject();
//    						if(Task.class.isAssignableFrom(ob.getClass())) {
//    							r = (Task) ob;
//    						}else {
//    							logger.error(ob.getClass().getName()+"--不是此线程池可以执行的方法");
//    							r = null;
//    						}
//                    	}
                    	r = takeFromHead();
					}catch (Exception e) {
						// TODO Auto-generated catch block
						logger.error("获得任务失败", e);
						r = null;
					}
                }
                if (r != null) {
                    isWaiting = false;
                    try {
                        /* 该任务是否需要立即执行 */
                        if (r.needExecuteImmediate()) {
                            new Thread(r).start();
                        } else {
                        	r.setBeginExceuteTime(new Date());
                            r.run();
                            r.setFinishTime(new Date());
                        }
                    } catch (Exception e) {
                        logger.error(e);
                    }
                    isWaiting = true;
                    r = null;
                }
            }
        }
    }
    public void setKey(String key) {  
        this.key = key;  
    }
	public int getWorker_num() {
		return worker_num;
	}
	public void setWorker_num(int worker_num) {
		this.worker_num = worker_num;
	}
}
