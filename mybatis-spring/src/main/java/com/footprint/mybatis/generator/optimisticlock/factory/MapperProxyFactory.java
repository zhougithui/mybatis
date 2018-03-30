package com.footprint.mybatis.generator.optimisticlock.factory;

import com.footprint.mybatis.generator.optimisticlock.handler.OptimisticLockHandler;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

@SuppressWarnings("all")
public class MapperProxyFactory<T> implements FactoryBean<T>, InitializingBean {

	private Class<T> mapperInterface;

	private SqlSession sqlSession;

	private boolean externalSqlSession = Boolean.FALSE;

	private boolean isSingleton = Boolean.TRUE;

	private T proxy;

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		if (!this.externalSqlSession) {
			this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
		}
	}

	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		this.sqlSession = sqlSessionTemplate;
		this.externalSqlSession = true;
	}

	public void setMapperInterface(Class<T> mapperInterface) {
		this.mapperInterface = mapperInterface;
	}

	@Override
	public T getObject() throws Exception {
		return proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return this.mapperInterface;
	}

	@Override
	public boolean isSingleton() {
		return MapperProxyFactory.this.isSingleton;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.mapperInterface,
				"Property 'mapperInterface' is required");
		if (null != sqlSession) {
			Object mapper = sqlSession.getMapper(mapperInterface);
			if (null != mapper)
				proxy = (T) new OptimisticLockHandler(mapper).getInstance();
		}
	}
}