package com.liu.springboot.quickstart.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.liu.springboot.quickstart.aop.annotation.ParameterNotBlank;
import com.liu.springboot.quickstart.service.RedisService;

@Service(value="redisService")
public class RedisServiceImpl implements RedisService {

	@Autowired
    private @Qualifier("redisTemplate")RedisTemplate<String, Object> redisTemplate;
    
	@ParameterNotBlank
	@Override
	public Object getSet(String key, String value, long liveTime) {
		// TODO Auto-generated method stub
		Object a = redisTemplate.opsForValue().getAndSet(key, value);
		if(liveTime > 0) {
			redisTemplate.expire(key, liveTime, TimeUnit.SECONDS);
		}
		return a;
	}
	@ParameterNotBlank
	@Override
	public long delForValue(List<String> keys) throws NullPointerException{
		// TODO Auto-generated method stub
		redisTemplate.delete(keys);
		return 1L;
	}

	@ParameterNotBlank
	@Override
	public void set(String key, Object value, long liveTime) throws NullPointerException{
		// TODO Auto-generated method stub
		if(liveTime > 0) {
			redisTemplate.opsForValue().set(key, value, liveTime, TimeUnit.SECONDS);
		}else {
			throw new NullPointerException("存活时间必须大于0!!!");
		}
	}
	
	@ParameterNotBlank
	@Override
	public void setByOffset(String key, Object value, long offset) throws NullPointerException{
		// TODO Auto-generated method stub
		if(offset > 0) {
			redisTemplate.opsForValue().set(key, value, offset);
		}else {
			throw new NullPointerException("偏移量必须大于0!!!");
		}
		
	}
	
	@ParameterNotBlank
	@Override
	public Boolean setValueForExist(String key, String value) {
		// TODO Auto-generated method stub
		return redisTemplate.opsForValue().setIfAbsent(key, value);
	}

	@ParameterNotBlank
	@Override
	public void set(String key, Object value) {
		// TODO Auto-generated method stub
		redisTemplate.opsForValue().set(key, value);
	}

	@ParameterNotBlank
	@Override
	public Object get(String key) {
		// TODO Auto-generated method stub
		return redisTemplate.opsForValue().get(key);
	}

	@ParameterNotBlank
	@Override
	public Object getkeys(String pattern) {
		// TODO Auto-generated method stub
		 return redisTemplate.keys(pattern);
	}

	@ParameterNotBlank
	@Override
	public boolean exists(final String key) {
		// TODO Auto-generated method stub
		return redisTemplate.hasKey(key);
	}

	@Override
	public String flushDB() {
		// TODO Auto-generated method stub
        return redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
	}

	@Override
	public long dbSize() {
		// TODO Auto-generated method stub
        return redisTemplate.execute(new RedisCallback<Long>() {
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.dbSize();
            }
        });
	}

	@Override
	public String ping() {
		// TODO Auto-generated method stub
        return redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.ping();
            }
        });
	}
}
