package com.footprint.mybatis.generator.optimisticlock.handler;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import com.footprint.mybatis.generator.optimisticlock.factory.MapperProxyFactory;

import java.util.Collection;

/**
 * 覆盖原有的mybatis注册的bean信息，修改获取bean的指向
 * org.apache.ibatis.binding.MapperProxyFactory指向com.lanmao.ec.mybatis.factory.MapperProxyFactory
 * 
 * @author zhou.hui 2017-08-22 14:39:42
 */
public class MapperScannerHandler {
	
	private BeanDefinitionRegistry registry;
	
	private String scope = "singleton";

	public MapperScannerHandler(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}

	public void dispose(final SqlSessionFactory sqlSessionFactory) {
		//Set<AbstractBeanDefinition> candidates = new LinkedHashSet<AbstractBeanDefinition>();
		Collection<Class<?>> collection = sqlSessionFactory.getConfiguration()
				.getMapperRegistry().getMappers();

		for (Class<?> candidate : collection) {
			BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
					.genericBeanDefinition(candidate);
			beanDefinitionBuilder.setScope(MapperScannerHandler.this.scope);
			AbstractBeanDefinition definition = beanDefinitionBuilder.getBeanDefinition();
			definition.getPropertyValues().add("mapperInterface",
					definition.getBeanClassName());
			definition.getPropertyValues().addPropertyValue("sqlSessionFactory", sqlSessionFactory);
			definition.setBeanClass(MapperProxyFactory.class);
			
			
			// 默认使用全类名进行注册,避免冲突的概率
			registry.registerBeanDefinition(candidate.getName(),
					definition);
			//candidates.add(definition);
		}
	}
}
