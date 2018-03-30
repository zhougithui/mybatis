package com.footprint.mybatis.generator.optimisticlock.handler;

import com.footprint.mybatis.generator.optimisticlock.annotation.OptimisticLock;
import com.footprint.mybatis.generator.optimisticlock.exception.OptimisticLockingException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class OptimisticLockHandler implements InvocationHandler {

	private String message = "OptimisticLockingException";

	private Object target;

	public OptimisticLockHandler(Object target) {
		this.target = target;
	}

	public Object getInstance() {
		return Proxy.newProxyInstance(target.getClass().getClassLoader(),
				target.getClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		try {
			result = method.invoke(target, args);
			if (checkMethodOptimisticLock(method)) {
				if (result instanceof Number && ((Number) result).longValue() < 1) {
					throw new OptimisticLockingException(
							OptimisticLockHandler.this.message);
				}
			}
			return result;
		} catch (InvocationTargetException e) {
			throw e.getCause();
		}
	}

	private Boolean checkMethodOptimisticLock(Method method) {
		OptimisticLock optimisticLock = method
				.getAnnotation(OptimisticLock.class);
		if (optimisticLock != null) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
