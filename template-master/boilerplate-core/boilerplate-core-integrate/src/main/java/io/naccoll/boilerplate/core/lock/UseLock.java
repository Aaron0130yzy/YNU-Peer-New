package io.naccoll.boilerplate.core.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseLock {

	/**
	 * 上锁用的key，通常是数据记录的id
	 * @return
	 */
	String key() default "";

	/**
	 * 上锁用的key的前缀，通常是数据记录的类型，但也可以为空
	 * @return
	 */
	String prefix() default "";

	LockMode lockMode() default LockMode.TRY_LOCK;

	long waitTime() default 3000L;

}
