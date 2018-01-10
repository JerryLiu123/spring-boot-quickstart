package com.liu.springboot.quickstart.config.redisconfig;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisPoolConfig;


@Configuration
@EnableCaching //启用缓存
@ConfigurationProperties(prefix="spring.redis.cn")//配置文件前缀
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RedisConfig extends CachingConfigurerSupport {
	
    private String database;
    private String host;
    private int port;
    private String password;
    private int timeout = 15000;
    private int maxTotal = 10;
    private long maxWait = 1000;
    private int maxIdle = 8;
    private int minIdle = 0;

    @Bean(name="keyGenerator")
    public KeyGenerator keyGenerator() {
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getSimpleName()).append(":");//执行方法所在的类
			sb.append(Stream.of(params).map(String::valueOf).collect(Collectors.joining("_")));
			return sb.toString();
		};
    }
    /**
     * 管理缓存
     * @param redisTemplate
     * @return
     */
	@Bean
    public CacheManager cacheManager(@Qualifier("redisTemplate")RedisTemplate redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        //设置缓存过期时间
        rcm.setDefaultExpiration(60);//秒
        return rcm;
    }
    /**
     * RedisTemplate配置
     * @param factory
     * @return
     */
	@Bean(name="redisTemplate")
    public RedisTemplate<?, ?> redisTemplate(@Qualifier("jedisConnectionFactory")RedisConnectionFactory factory) {
    	RedisTemplate template = new RedisTemplate();
    	template.setConnectionFactory(factory);
		setMySerializer(template);
		template.afterPropertiesSet();
		return template;
    }
    
	@Bean(name="jedisPoolConfig")
    public JedisPoolConfig jedisPoolConfig() {
	    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
	    jedisPoolConfig.setMaxTotal(this.maxTotal);
	    jedisPoolConfig.setMaxIdle(this.maxIdle);
	    jedisPoolConfig.setMinIdle(this.minIdle);
	    jedisPoolConfig.setMaxWaitMillis(this.maxWait);
        return jedisPoolConfig;
    }
	
	@Bean(name="jedisConnectionFactory")
	public JedisConnectionFactory jedisConnectionFactory() {
	    JedisConnectionFactory factory = new JedisConnectionFactory();
	    factory.setPoolConfig(this.jedisPoolConfig());
	    factory.setHostName(this.host);
	    factory.setPort(this.port);
	    if(!StringUtils.isEmpty(this.password)) {
	        factory.setPassword(this.password);
	    }
	    factory.setTimeout(this.timeout);
	    factory.setUsePool(true);
	    factory.afterPropertiesSet();
	    return factory;
	}
    
    /**
     * 序列化方法
     * @param template
     */
	private void setMySerializer(RedisTemplate template) {
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setKeySerializer(template.getStringSerializer());//设置key的序列化方式为String
		template.setValueSerializer(jackson2JsonRedisSerializer);//设置value的序列化方法,转换为json
	}
	
	/**
	 * 这里注入RedissonClient暂时只是用到了 其中分布式锁的功能，其他的redis操作没有迁移
	 * @return
	 */
	@Bean
	public RedissonClient redissonClient() {
	    Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress("redis://"+this.host+":"+this.port)
                .setTimeout(this.timeout)
                .setConnectionPoolSize(this.maxTotal)
                .setConnectionMinimumIdleSize(this.minIdle);
        if(!StringUtils.isEmpty(this.password)) {
            serverConfig.setPassword(this.password);
        }

        return Redisson.create(config);	    
	}
	
    public String getDatabase() {
        return database;
    }
    public void setDatabase(String database) {
        this.database = database;
    }
    public String getHost() {
        return host;
    }
    public void setHost(String host) {
        this.host = host;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getMaxTotal() {
        return maxTotal;
    }
    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }
 
    public int getMaxIdle() {
        return maxIdle;
    }
    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }
    public long getMaxWait() {
        return maxWait;
    }
    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }
    public int getMinIdle() {
        return minIdle;
    }
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }
    public int getTimeout() {
        return timeout;
    }
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
	
	
}
