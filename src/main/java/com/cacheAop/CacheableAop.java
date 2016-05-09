package com.cacheAop;

import java.lang.reflect.Method;
import java.util.Properties;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import com.utils.PropertiesUtils;

public class CacheableAop {
    private static Logger  log	      = LoggerFactory.getLogger(CacheableAop.class);
    private RedisCacheBean redis;
    private Properties	   properties = PropertiesUtils.loadPropertyInstance("config.properties");

    public void setRedis(RedisCacheBean redis) {
	this.redis = redis;
    }

    // @Around("@annotation(com.cbcs.core.cacheAop.Cacheable)")
    @SuppressWarnings("unchecked")
    public Object cached(final ProceedingJoinPoint pjp) throws Throwable {
	boolean cacheEnable = false;
	try {
	    String pval = this.properties.get("cacheEnable") == null ? "false"
		    : this.properties.get("cacheEnable").toString();
	    cacheEnable = Boolean.valueOf(pval);
	} catch (Exception e) {
	    if (log.isErrorEnabled())
		log.error("缓存配置{}在配置文件{}中读取异常{}", "cacheEnable", "config.properties", e.getMessage());
	}
	if (log.isDebugEnabled())
	    log.debug("我在aop方法执行之前===============");
	if (!cacheEnable) {
	    return pjp.proceed();
	}
	Object result = null;
	Method method = getMethod(pjp);
	Cacheable cacheable = method.getAnnotation(Cacheable.class);

	String key = getKey(pjp);
	// 给注解设置值时
	if (cacheable.key() != null && !"".equals(cacheable.key())) {
	    key = cacheable.key();
	}

	String fieldKey = parseKey(key, method, pjp.getArgs());

	// 获取方法的返回类型,让缓存可以返回正确的类型
	@SuppressWarnings("rawtypes")
	Class returnType = ((MethodSignature) pjp.getSignature()).getReturnType();

	// 使用redis 的hash进行存取，易于管理
	result = redis.hget(key, fieldKey, returnType);

	if (result == null) {
	    try {
		result = pjp.proceed();
		Assert.notNull(fieldKey);
		redis.hset(key, fieldKey, cacheable.expireTime(), result);
		if (log.isDebugEnabled())
		    log.debug("我在aop方法执行之后===============");
	    } catch (Throwable e) {
		e.printStackTrace();
	    }
	}
	if (log.isDebugEnabled())
	    log.debug("我在aop方法执行之后===============");
	return result;
    }

    /** * 定义清除缓存逻辑 */
    // @Around(value = "@annotation(com.cbcs.core.cacheAop.CacheEvict)")
    public void evict(ProceedingJoinPoint pjp) {
	if (log.isDebugEnabled())
	    log.debug("我在aop方法执行之前===============");
	Object result = null;
	Method method = getMethod(pjp);
	Cacheable cacheable = method.getAnnotation(Cacheable.class);
	String fieldKey = parseKey(cacheable.fieldKey(), method, pjp.getArgs());

	// 使用redis 的hash进行存取，易于管理
	result = redis.hget(cacheable.key(), fieldKey);

	if (result != null) {
	    try {
		result = pjp.proceed();
		Assert.notNull(fieldKey);
		redis.hdel(cacheable.key(), fieldKey);
		if (log.isDebugEnabled())
		    log.debug("我在aop方法执行之后===============");
	    } catch (Throwable e) {
		e.printStackTrace();
	    }
	}
	if (log.isDebugEnabled())
	    log.debug("我在aop之最后===============");
    }

    // 获取 主键 为 主键值，由系统名称+类名，删除时用
    public String getKey(ProceedingJoinPoint pjp) {
	String prefixKey = "mainKey";
	try {
	    prefixKey = this.properties.get("prefixKey") == null ? "mainKey"
		    : this.properties.get("prefixKey").toString();
	} catch (Exception e) {
	    if (log.isErrorEnabled())
		log.error("缓存配置{}在配置文件{}中读取异常{}", "prefixKey", "config/config.properties", e.getMessage());
	}
	@SuppressWarnings("rawtypes")
	Class claz = pjp.getTarget().getClass();
	// 取得类名
	String className = claz.getSimpleName();
	Package package1 = claz.getPackage();

	if (prefixKey == null || "".equals(prefixKey)) {
	    prefixKey = "mainKey";
	}
	// 主键值，由系统名称+类名，删除时用
	String mainKey = "#" + prefixKey + "-" + package1.getName() + "." + className;
	return mainKey;
    }

    /**
     * 获取被拦截方法对象
     * 
     * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象 而缓存的注解在实现类的方法上
     * 所以应该使用反射获取当前对象的方法对象
     */
    public Method getMethod(ProceedingJoinPoint pjp) {
	// 获取参数的类型
	Object[] args = pjp.getArgs();
	@SuppressWarnings("rawtypes")
	Class[] argTypes = new Class[pjp.getArgs().length];
	for (int i = 0; i < args.length; i++) {
	    argTypes[i] = args[i].getClass();
	}
	Method method = null;
	try {
	    method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
	} catch (NoSuchMethodException e) {
	    e.printStackTrace();
	} catch (SecurityException e) {
	    e.printStackTrace();
	}
	return method;

    }

    /**
     * 获取缓存的key key 定义在注解上，支持SPEL表达式
     * 不是spel 表達式  使用方法名+參數名稱
     * @param pjp
     * @return
     */
    private String parseKey(String key, Method method, Object[] args) {
	// 获取被拦截方法参数名列表(使用Spring支持类库)
	LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
	String[] paraNameArr = u.getParameterNames(method);
	String res = "";
	try {
	    // 使用SPEL进行key的解析
	    ExpressionParser parser = new SpelExpressionParser();
	    // SPEL上下文
	    StandardEvaluationContext context = new StandardEvaluationContext();
	    // 把方法参数放入SPEL上下文中
	    for (int i = 0; i < paraNameArr.length; i++) {
		context.setVariable(paraNameArr[i], args[i]);
	    }
	    res = parser.parseExpression(key).getValue(context, String.class);
	} catch (Exception e) {
	    if (log.isErrorEnabled()) {
		log.error("SPEL表达式转换异常", e);
	    }
	    res = method.getName();
	    // 把方法参数放入SPEL上下文中
	    for (int i = 0; i < paraNameArr.length; i++) {
		res += ":" + paraNameArr[i];
	    }
	}
	return res;
    }

}