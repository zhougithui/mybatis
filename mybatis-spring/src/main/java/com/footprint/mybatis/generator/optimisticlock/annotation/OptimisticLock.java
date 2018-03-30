package com.footprint.mybatis.generator.optimisticlock.annotation;

import java.lang.annotation.*;


@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OptimisticLock {
	
}
