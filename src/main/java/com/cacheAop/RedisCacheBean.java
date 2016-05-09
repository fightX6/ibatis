package com.cacheAop;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;

public class RedisCacheBean {
    private JedisUtil jedisUtil;
    
    public void setJedisUtil(JedisUtil jedisUtil) {
	this.jedisUtil = jedisUtil;
    }
    /**
     * 把对象放入Hash中
     */
    public void hset(String key, String field,int expire, Object o) {
	Jedis jedis = jedisUtil.getJedis();
	jedis.hset(key, field, JSON.toJSONString(o));
	if(expire!=0) jedisUtil.expire(key, expire);
	jedisUtil.returnJedis(jedis);
    }

    /**
     * 从Hash中获取对象
     */
    public String hget(String key, String field) {
	Jedis jedis = jedisUtil.getJedis();
	String text = jedis.hget(key, field);
	jedisUtil.returnJedis(jedis);
	return text;
    }

    /**
     * 从Hash中获取对象,转换成制定类型
     */
    public <T> T hget(String key, String field, Class<T> clazz) {
	String text = hget(key, field);
	T result = JSON.parseObject(text, clazz);
	return result;
    }

    /**
     * 从Hash中删除对象
     */
    public void hdel(String key, String... field) {
	Jedis jedis = jedisUtil.getJedis();
	Object result = jedis.hdel(key, field);
	jedisUtil.returnJedis(jedis);
    }
}
