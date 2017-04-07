package com.rz.data.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil
{
	private static JedisPoolConfig c = new JedisPoolConfig();
	private static JedisPool jedisPool = null;

	static
	{
		c.setBlockWhenExhausted(true);
		c.setLifo(true);
		c.setMaxIdle(10);
		c.setMinIdle(0);
		c.setMaxTotal(20);
		c.setMaxWaitMillis(-1);
		c.setMinEvictableIdleTimeMillis(1800000);
		c.setTestOnBorrow(true);
		c.setTestWhileIdle(true);
		jedisPool = new JedisPool(c, "11.0.0.33", 6379);
	}

	/**
	 * 获取Jedis连接
	 * 
	 * @return Jedis连接
	 */
	public static Jedis getJedis()
	{
		return jedisPool.getResource();
	}
}
