package com.footprint.mybatis.generator.optimisticlock.factory;

import com.footprint.mybatis.generator.optimisticlock.handler.MapperScannerHandler;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class MapperRepositoryFactory implements ApplicationContextAware, InitializingBean {
	
	private ApplicationContext applicationContext;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
		ConfigurableApplicationContext c = (ConfigurableApplicationContext)applicationContext;
		
		MapperScannerHandler scanner = new MapperScannerHandler(
				(BeanDefinitionRegistry) c.getBeanFactory());
		scanner.dispose(sqlSessionFactory);
	}
}