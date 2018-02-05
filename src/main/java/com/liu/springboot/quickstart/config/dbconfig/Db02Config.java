package com.liu.springboot.quickstart.config.dbconfig;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.github.pagehelper.PageHelper;
import com.liu.springboot.quickstart.util.ArrayUtils;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

/**
 * db02 数据库配置
 * @author 原作者:Jfei
 * @author 修改:lgh
 *
 */

@ConfigurationProperties(prefix="spring.datasource.db02")
@Configuration
@MapperScan(basePackages= {"com.liu.springboot.quickstart.dao.db02dao","com.liu.springboot.quickstart.dao.basedao"},sqlSessionTemplateRef="sqlSessionTemplate")
//@MapperScan(basePackages= {"com.liu.springboot.quickstart.dao.db02dao"}, sqlSessionTemplateRef="db02SqlSessionTemplate")
public class Db02Config {

   private Logger logger = LoggerFactory.getLogger(Db02Config.class);
	
   private String url;
   private String username;
   private String password;

   /** min-pool-size 最小连接数 **/
   private Integer minPoolSize;
   /** max-pool-size 最大连接数 **/
   private Integer maxPoolSize;
   /** max-lifetime 连接最大存活时间 **/
   private int maxLifetime;
   /** borrow-connection-timeout 获取连接失败重新获等待最大时间，在这个时间内如果有可用连接，将返回 **/
   private Integer borrowConnectionTimeout;
   /** login-timeout java数据库连接池，最大可等待获取datasouce的时间 **/
   private Integer loginTimeout;
   /** maintenance-interval 连接回收时间 **/
   private Integer maintenanceInterval;
   /** max-idle-time 最大闲置时间，超过最小连接池连接的连接将将关闭 **/
   private Integer maxIdleTime;
   /** test-query 测试SQL **/
   private String testQuery;
   /**driverClassName**/
   private String driverClassName;
   /**最大执行等待时间**/
   private Integer maxWaitTime;

// 配置mapper的扫描，找到所有的mapper.xml映射文件
   private String mapperLocations;
   private String baseMapperLocations;

// 加载全局的配置文件
   private String configLocation;
   
   
// 配置数据源
	@Bean(name = "db02DataSource")
	public DataSource db01DataSource() throws SQLException {
		
//      MysqlXADataSource druidXADataSource = new MysqlXADataSource();
//      druidXADataSource.setUrl(url);
//      druidXADataSource.setPinGlobalTxToPhysicalConnection(true);
//      druidXADataSource.setPassword(password);
//      druidXADataSource.setUser(username);
//      druidXADataSource.setPinGlobalTxToPhysicalConnection(true);
        
//        DruidXADataSource druidXADataSource = new DruidXADataSource();
//        druidXADataSource.setName("db02DataSource");
//        druidXADataSource.setDriverClassName(driverClassName);
//        druidXADataSource.setDbType("mysql");
//        druidXADataSource.setDefaultAutoCommit(false);
//        druidXADataSource.setUrl(url);
//        druidXADataSource.setUsername(username);
//        druidXADataSource.setPassword(password);
//        druidXADataSource.setInitialSize(minPoolSize);
//        druidXADataSource.setMinIdle(minPoolSize);
//        druidXADataSource.setMaxActive(maxPoolSize);
//        druidXADataSource.setMaxWait(maxWaitTime);
//        druidXADataSource.setTestOnBorrow(false);
//        druidXADataSource.setTestOnReturn(false);
//        druidXADataSource.setTestWhileIdle(true);
//        druidXADataSource.setTimeBetweenEvictionRunsMillis(200000);
//        druidXADataSource.setValidationQuery(testQuery);
//        druidXADataSource.setValidationQueryTimeout(2000);
//        druidXADataSource.setRemoveAbandoned(true);
//        druidXADataSource.setRemoveAbandonedTimeout(1800);
//        druidXADataSource.setPoolPreparedStatements(false);
//        druidXADataSource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolSize);
//        druidXADataSource.setFilters("stat");
//        druidXADataSource.setLogAbandoned(true);
        
        
        Properties xaProperties = new Properties();
        xaProperties.put("driverClassName", driverClassName);
        xaProperties.put("dbType", "mysql");
        xaProperties.put("defaultAutoCommit", false);
        xaProperties.put("url", url);
        xaProperties.put("username", username);
        xaProperties.put("password", password);
        xaProperties.put("initialSize", minPoolSize);
        xaProperties.put("minIdle", minPoolSize);
        xaProperties.put("maxActive", maxPoolSize);
        xaProperties.put("maxWait", maxWaitTime);
        xaProperties.put("testOnBorrow", false);//在从池中取出链接时是否检查,设置为false，以加快取出速度
        xaProperties.put("testOnReturn", false);//返回链接时是否检测,设置为false，以加快取出速度
        xaProperties.put("testWhileIdle", true);//设置定时检查链接可用性
        xaProperties.put("timeBetweenEvictionRunsMillis", Integer.valueOf(200000));//检查链接可用性间隔
        xaProperties.put("validationQuery", testQuery);//检测链接所用的sql
        xaProperties.put("validationQueryTimeout", Integer.valueOf(2000));
        xaProperties.put("removeAbandoned", false);
        //xaProperties.put("removeAbandonedTimeout", Integer.valueOf(300));
        xaProperties.put("poolPreparedStatements", true);       
        xaProperties.put("maxPoolPreparedStatementPerConnectionSize", maxPoolSize);
        xaProperties.put("filters", "mergeStat");
        
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        //xaDataSource.setXaDataSource(druidXADataSource);
        xaDataSource.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");//阿里的数据库连接
        xaDataSource.setUniqueResourceName("db02DataSource");

        xaDataSource.setMinPoolSize(minPoolSize);
        xaDataSource.setMaxPoolSize(maxPoolSize);
        xaDataSource.setMaxLifetime(maxLifetime);
        //xaDataSource.setBorrowConnectionTimeout(borrowConnectionTimeout);
        xaDataSource.setLoginTimeout(loginTimeout);
        xaDataSource.setMaintenanceInterval(maintenanceInterval);
        xaDataSource.setMaxIdleTime(maxIdleTime);
        //xaDataSource.setTestQuery(testQuery);
        xaDataSource.setXaProperties(xaProperties);
        try {
            xaDataSource.afterPropertiesSet();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("初始化数据连接错误!!!", e);
        }
        return xaDataSource;
	}
	
    @Bean("db02jdbcTemplate")
    public JdbcTemplate jdbcTemplate(@Qualifier("db02DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
	
	@Bean(name = "db02SqlSessionFactory")
	public SqlSessionFactory db01SqlSessionFactory(@Qualifier("db02DataSource") DataSource dataSource)
			throws Exception {
		
		  try {
             SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
             sessionFactoryBean.setDataSource(dataSource);
             
             //设置mapper.xml文件所在位置 
             //Resource[] resources = new PathMatchingResourcePatternResolver().getResources(this.mapperLocations);
             ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
             Resource[] resources = resolver.getResources(this.mapperLocations);
             Resource[] resourcesBase = resolver.getResources(this.baseMapperLocations);
             sessionFactoryBean.setMapperLocations(ArrayUtils.concat(resourcesBase, resources));
             //设置mybatis-config.xml配置文件位置
             sessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource(this.configLocation));

             //添加分页插件、打印sql插件
             Interceptor[] plugins = new Interceptor[]{pageHelper(),sqlPrintInterceptor()};
             sessionFactoryBean.setPlugins(plugins);
             
             return sessionFactoryBean.getObject();
         } catch (IOException e) {
             logger.error("mybatis resolver db01 mapper*xml is error",e);
             throw e;
         } catch (Exception e) {
             logger.error("mybatis db01sqlSessionFactoryBean create error",e);
             throw e;
         }
	}

//	@Bean(name = "db02SqlSessionTemplate")
//	public SqlSessionTemplate db01SqlSessionTemplate(
//			@Qualifier("db02SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//		return new SqlSessionTemplate(sqlSessionFactory);
//	}
	
	  /**
    * 分页插件
    * @param dataSource
    * @return
    */
 
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        p.setProperty("returnPageInfo", "check");
        p.setProperty("params", "count=countSql");
        pageHelper.setProperties(p);
        return pageHelper;
    }
    
    //将要执行的sql进行日志打印(不想拦截，就把这方法注释掉)
    public SqlPrintInterceptor sqlPrintInterceptor(){
    	return new SqlPrintInterceptor();
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(Integer minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(int maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public Integer getBorrowConnectionTimeout() {
        return borrowConnectionTimeout;
    }

    public void setBorrowConnectionTimeout(Integer borrowConnectionTimeout) {
        this.borrowConnectionTimeout = borrowConnectionTimeout;
    }

    public Integer getLoginTimeout() {
        return loginTimeout;
    }

    public void setLoginTimeout(Integer loginTimeout) {
        this.loginTimeout = loginTimeout;
    }

    public Integer getMaintenanceInterval() {
        return maintenanceInterval;
    }

    public void setMaintenanceInterval(Integer maintenanceInterval) {
        this.maintenanceInterval = maintenanceInterval;
    }

    public Integer getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(Integer maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public String getTestQuery() {
        return testQuery;
    }

    public void setTestQuery(String testQuery) {
        this.testQuery = testQuery;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public Integer getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(Integer maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public String getMapperLocations() {
        return mapperLocations;
    }

    public void setMapperLocations(String mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getBaseMapperLocations() {
        return baseMapperLocations;
    }

    public void setBaseMapperLocations(String baseMapperLocations) {
        this.baseMapperLocations = baseMapperLocations;
    }

    public String getConfigLocation() {
        return configLocation;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

}
