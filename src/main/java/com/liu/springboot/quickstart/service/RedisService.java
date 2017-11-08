package com.liu.springboot.quickstart.service;

import java.util.List;

/**
 * redis 操作，此处方法只是针对 redis 中String数据结构
 * @author lgh
 *
 */
public interface  RedisService {

	/**
	 * 删除key
	 * @param keys
	 * @return
	 * @throws NullPointerException
	 */
    public abstract long delForValue(List<String> keys) throws NullPointerException;


    /**
     * 添加key value 并且设置存活时间
     * 
     * @param key
     * @param value
     * @param liveTime
     *            单位秒
     * @throws NullPointerException
     */
    public abstract void set(String key, Object value, long liveTime) throws NullPointerException;

    /**
     * 添加key value
     * 
     * @param key
     * @param value
     */
    public abstract void set(String key, Object value);
    
    /**
     * 设置键值，并设置偏移量
     * @param key
     * @param value
     * @param offset
     * @throws NullPointerException
     */
    public abstract void setByOffset(String key, Object value, long offset) throws NullPointerException;

    /**
     * 获取redis value (String)
     * 
     * @param key
     * @return
     */
    public abstract Object get(String key);

    /**
     * 通过正则匹配keys
     * 
     * @param pattern
     * @return 
     */
    public abstract Object getkeys(String pattern);

    /**
     * 检查key是否已经存在
     * 
     * @param key
     * @return
     */
    public abstract boolean exists(String key);

    /**
     * 清空redis 所有数据
     * 
     * @return
     */
    public abstract String flushDB();

    /**
     * 查看redis里有多少数据
     */
    public abstract long dbSize();
    
    /**
     * 根据key是否存在添加 如果 key存在则返回false，不存在则添加key后返回true
     * @param key
     * @param value
     * @return
     */
    public Boolean setValueForExist(final String key, final String value);
    
    /**
     * 设置新的值，并返回旧的值
     * @param key
     * @param value
     * @param liveTime
     * @return
     */
    public Object getSet(final String key, final String value, final long liveTime);

    /**
     * 检查是否连接成功
     * 
     * @return
     */
    public abstract String ping();
}
