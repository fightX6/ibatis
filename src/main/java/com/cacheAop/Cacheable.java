package com.cacheAop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Cacheable {
	
    String key() default "";//缓存key值
    String fieldKey()  default "" ;//缓存field值  暂时无用
    int expireTime() default 3600;//缓存时间多少秒   0为无期限
	
}