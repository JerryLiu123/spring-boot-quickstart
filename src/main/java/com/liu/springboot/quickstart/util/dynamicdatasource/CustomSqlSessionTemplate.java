package com.liu.springboot.quickstart.util.dynamicdatasource;


import static java.lang.reflect.Proxy.newProxyInstance;
import static org.apache.ibatis.reflection.ExceptionUtil.unwrapThrowable;
import static org.mybatis.spring.SqlSessionUtils.closeSqlSession;
import static org.mybatis.spring.SqlSessionUtils.getSqlSession;
import static org.mybatis.spring.SqlSessionUtils.isSqlSessionTransactional;
import static org.springframework.util.Assert.notNull;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.util.Assert;

/**
 * 重写某些方法，达到动态设置SqlSessionFactory的效果
 * @author lgh
 *
 */
public class CustomSqlSessionTemplate extends SqlSessionTemplate {

    private Map<Object, SqlSessionFactory> targetSqlSessionFactorys;  
    private SqlSessionFactory defaultTargetSqlSessionFactory;  
    
    /*被继承方法中的变量*/
    private final SqlSessionFactory sqlSessionFactory;

    private final ExecutorType executorType;

    private final SqlSession sqlSessionProxy;

    private final PersistenceExceptionTranslator exceptionTranslator;
    
	
    public void setTargetSqlSessionFactorys(Map<Object, SqlSessionFactory> targetSqlSessionFactorys) {  
        this.targetSqlSessionFactorys = targetSqlSessionFactorys;  
    }  
    
    public void setDefaultTargetSqlSessionFactory(SqlSessionFactory defaultTargetSqlSessionFactory) {  
        this.defaultTargetSqlSessionFactory = defaultTargetSqlSessionFactory;  
    }  
    
    
    /*构造方法开始*/
    public CustomSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {  
    	this(sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType());
    }  
    
    public CustomSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType) {  
        this(sqlSessionFactory, executorType,
                new MyBatisExceptionTranslator(
                    sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), true));
    }  
    
    public CustomSqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType,  
            PersistenceExceptionTranslator exceptionTranslator) {  
        
        super(sqlSessionFactory, executorType, exceptionTranslator); 
        notNull(sqlSessionFactory, "Property 'sqlSessionFactory' is required");
        notNull(executorType, "Property 'executorType' is required");
        this.sqlSessionFactory = sqlSessionFactory;
        this.executorType = executorType;
        this.exceptionTranslator = exceptionTranslator;
        this.sqlSessionProxy = (SqlSession) newProxyInstance(
            SqlSessionFactory.class.getClassLoader(),
            new Class[] { SqlSession.class },
            new SqlSessionInterceptor());
        
        this.defaultTargetSqlSessionFactory = sqlSessionFactory; 
    } 
    /*构造方法结束*/
    
    @Override  
    public Configuration getConfiguration() {  
        return this.getSqlSessionFactory().getConfiguration();  
    }  
    
    /**
     * 重新 获得数据连接工厂方法，根据情况获得数据工厂
     */
    @Override  
    public SqlSessionFactory getSqlSessionFactory() {  
        SqlSessionFactory targetSqlSessionFactory = targetSqlSessionFactorys.get(CustomerContextHolder.getContextType());  
        Assert.notNull(defaultTargetSqlSessionFactory, "Property 'defaultTargetSqlSessionFactory' are required");  
        if (targetSqlSessionFactory != null) {  
            return targetSqlSessionFactory;  
        } else if (this.defaultTargetSqlSessionFactory != null) {  
            //System.err.println("获得targetSqlSessionFactory为空,使用默认的SqlSessionFactory:"+this.defaultTargetSqlSessionFactory.hashCode());
            return this.defaultTargetSqlSessionFactory;  
        }
        return this.sqlSessionFactory;
    }  
    public ExecutorType getExecutorType() {
    	return this.executorType;
	}

	public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
		return this.exceptionTranslator;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T selectOne(String statement) {
      return this.sqlSessionProxy.<T> selectOne(statement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T selectOne(String statement, Object parameter) {
      return this.sqlSessionProxy.<T> selectOne(statement, parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
      return this.sqlSessionProxy.<K, V> selectMap(statement, mapKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
      return this.sqlSessionProxy.<K, V> selectMap(statement, parameter, mapKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
      return this.sqlSessionProxy.<K, V> selectMap(statement, parameter, mapKey, rowBounds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Cursor<T> selectCursor(String statement) {
      return this.sqlSessionProxy.selectCursor(statement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter) {
      return this.sqlSessionProxy.selectCursor(statement, parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds) {
      return this.sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> List<E> selectList(String statement) {
      return this.sqlSessionProxy.<E> selectList(statement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> List<E> selectList(String statement, Object parameter) {
      return this.sqlSessionProxy.<E> selectList(statement, parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
      return this.sqlSessionProxy.<E> selectList(statement, parameter, rowBounds);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void select(String statement, ResultHandler handler) {
      this.sqlSessionProxy.select(statement, handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void select(String statement, Object parameter, ResultHandler handler) {
      this.sqlSessionProxy.select(statement, parameter, handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler) {
      this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int insert(String statement) {
      return this.sqlSessionProxy.insert(statement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int insert(String statement, Object parameter) {
      return this.sqlSessionProxy.insert(statement, parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(String statement) {
      return this.sqlSessionProxy.update(statement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int update(String statement, Object parameter) {
      return this.sqlSessionProxy.update(statement, parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(String statement) {
      return this.sqlSessionProxy.delete(statement);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int delete(String statement, Object parameter) {
      return this.sqlSessionProxy.delete(statement, parameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getMapper(Class<T> type) {
      return getConfiguration().getMapper(type, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
      throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit(boolean force) {
      throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback() {
      throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rollback(boolean force) {
      throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
      throw new UnsupportedOperationException("Manual close is not allowed over a Spring managed SqlSession");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCache() {
      this.sqlSessionProxy.clearCache();
    }

    /**
     * {@inheritDoc}
     *
     */
//    @Override
//    public Configuration getConfiguration() {
//      return this.sqlSessionFactory.getConfiguration();
//    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection getConnection() {
      return this.sqlSessionProxy.getConnection();
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.2
     *
     */
    @Override
    public List<BatchResult> flushStatements() {
      return this.sqlSessionProxy.flushStatements();
    }

    /**
    * Allow gently dispose bean:
    * <pre>
    * {@code
    *
    * <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
    *  <constructor-arg index="0" ref="sqlSessionFactory" />
    * </bean>
    * }
    *</pre>
    *
    * The implementation of {@link DisposableBean} forces spring context to use {@link DisposableBean#destroy()} method instead of {@link SqlSessionTemplate#close()} to shutdown gently.
    *
    * @see SqlSessionTemplate#close()
    * @see org.springframework.beans.factory.support.DisposableBeanAdapter#inferDestroyMethodIfNecessary
    * @see org.springframework.beans.factory.support.DisposableBeanAdapter#CLOSE_METHOD_NAME
    */
    @Override
    public void destroy() throws Exception {
    //This method forces spring disposer to avoid call of SqlSessionTemplate.close() which gives UnsupportedOperationException
    }    
    
    
    /** 
     * Proxy needed to route MyBatis method calls to the proper SqlSession got from Spring's Transaction Manager It also 
     * unwraps exceptions thrown by {@code Method#invoke(Object, Object...)} to pass a {@code PersistenceException} to 
     * the {@code PersistenceExceptionTranslator}. 
     */  
    private class SqlSessionInterceptor implements InvocationHandler {  
    	@Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {  
            SqlSession sqlSession = getSqlSession(  
                    CustomSqlSessionTemplate.this.getSqlSessionFactory(),  
                    CustomSqlSessionTemplate.this.executorType,   
                    CustomSqlSessionTemplate.this.exceptionTranslator);  
            try {  
                Object result = method.invoke(sqlSession, args);  
                if (!isSqlSessionTransactional(sqlSession, CustomSqlSessionTemplate.this.getSqlSessionFactory())) {  
                    // force commit even on non-dirty sessions because some databases require  
                    // a commit/rollback before calling close()  
                    sqlSession.commit(true);  
                }  
                return result;  
            }catch (Throwable t) {
                Throwable unwrapped = unwrapThrowable(t);
                if (CustomSqlSessionTemplate.this.exceptionTranslator != null && unwrapped instanceof PersistenceException) {
                  // release the connection to avoid a deadlock if the translator is no loaded. See issue #22
                  closeSqlSession(sqlSession, CustomSqlSessionTemplate.this.getSqlSessionFactory());
                  sqlSession = null;
                  Throwable translated = CustomSqlSessionTemplate.this.exceptionTranslator.translateExceptionIfPossible((PersistenceException) unwrapped);
                  if (translated != null) {
                    unwrapped = translated;
                  }
                }
                throw unwrapped;
              } finally {
                if (sqlSession != null) {
                  closeSqlSession(sqlSession, CustomSqlSessionTemplate.this.getSqlSessionFactory());
                }
              }  
        }  
    }  
}
