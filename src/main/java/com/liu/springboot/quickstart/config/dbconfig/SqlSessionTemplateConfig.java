package com.liu.springboot.quickstart.config.dbconfig;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.liu.springboot.quickstart.config.ConstantsConfig;
import com.liu.springboot.quickstart.util.dynamicdatasource.CustomSqlSessionTemplate;
import com.liu.springboot.quickstart.util.dynamicdatasource.DBEnum;

/**
 * 配置sqlSessionTemplate
 * 注入不同的sqlSessionFactory实现动态切换数据源
 * @author lgh
 *
 */
@Configuration
public class SqlSessionTemplateConfig {

    @Autowired
    @Qualifier("db01SqlSessionFactory") 
    SqlSessionFactory sqlSessionFactory01;
    
    @Autowired
    @Qualifier("db02SqlSessionFactory") 
    SqlSessionFactory sqlSessionFactory02;
	
	@Bean(name = "sqlSessionTemplate")
	public CustomSqlSessionTemplate sqlSessionTemplate() throws Exception {
		
		CustomSqlSessionTemplate sqlSessionTemplate = new CustomSqlSessionTemplate(sqlSessionFactory01);
		Map<Object, SqlSessionFactory> targetSqlSessionFactorys = new HashMap<Object, SqlSessionFactory>();
		targetSqlSessionFactorys.put(DBEnum.dataMySQL1, sqlSessionFactory01);
		targetSqlSessionFactorys.put(DBEnum.dataMySQL2, sqlSessionFactory02);
		sqlSessionTemplate.setTargetSqlSessionFactorys(targetSqlSessionFactorys);
		sqlSessionTemplate.setDefaultTargetSqlSessionFactory(sqlSessionFactory01);
		return sqlSessionTemplate;
	}
}
