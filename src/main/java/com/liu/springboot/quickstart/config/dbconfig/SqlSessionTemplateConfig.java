package com.liu.springboot.quickstart.config.dbconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.liu.springboot.quickstart.config.ConstantsConfig;
import com.liu.springboot.quickstart.util.dynamicdatasource.CustomSqlSessionTemplate;

@Configuration
public class SqlSessionTemplateConfig {

	
	@Bean(name = "sqlSessionTemplate")
	public CustomSqlSessionTemplate sqlSessionTemplate(
			@Qualifier("db01SqlSessionFactory") SqlSessionFactory sqlSessionFactory01, 
			@Qualifier("db02SqlSessionFactory") SqlSessionFactory sqlSessionFactory02) throws Exception {
		
		CustomSqlSessionTemplate sqlSessionTemplate = new CustomSqlSessionTemplate(sqlSessionFactory01);
		Map<Object, SqlSessionFactory> targetSqlSessionFactorys = new HashMap<Object, SqlSessionFactory>();
		targetSqlSessionFactorys.put(ConstantsConfig.DATESOURCE1, sqlSessionFactory01);
		targetSqlSessionFactorys.put(ConstantsConfig.DATESOURCE2, sqlSessionFactory02);
		sqlSessionTemplate.setTargetSqlSessionFactorys(targetSqlSessionFactorys);
		return sqlSessionTemplate;
	}
}
