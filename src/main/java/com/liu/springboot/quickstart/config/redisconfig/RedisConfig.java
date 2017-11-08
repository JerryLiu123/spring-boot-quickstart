package com.liu.springboot.quickstart.config.redisconfig;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;


@Configuration
@EnableCaching //启用缓存
//@ConfigurationProperties(prefix="spring.redis")//配置文件前缀
public class RedisConfig extends CachingConfigurerSupport {
	

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
    @SuppressWarnings("rawtypes")
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
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean(name="redisTemplate")
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory factory) {
    	RedisTemplate template = new RedisTemplate();
    	template.setConnectionFactory(factory);
		setMySerializer(template);
		template.afterPropertiesSet();
		return template;
    }
    
    /**
     * 序列化方法
     * @param template
     */
	@SuppressWarnings({ "rawtypes", "unchecked" })
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
}
