package com.rz.data.redis;

import java.util.List;
import redis.clients.jedis.Jedis;
import com.rz.data.redis.RedisUtil;

public class Test
{
	public static void main(String[] args)
	{
		// 连接本地的 Redis 服务
		Jedis jedis = RedisUtil.getJedis();
		System.out.println("Connection to server sucessfully");
		// 设置 redis 字符串数据
		jedis.set("runoobkey", "Redis tutorial");
		// 获取存储的数据并输出
		System.out.println("Stored string in redis:: " + jedis.get("runoobkey"));
		
		
		 //存储数据到列表中
	      jedis.lpush("tutorial-list", "Redis");
	      jedis.lpush("tutorial-list", "Mongodb");
	      jedis.lpush("tutorial-list", "Mysql");
	     // 获取存储的数据并输出
	     List<String> list = jedis.lrange("tutorial-list", 0 ,5);
	     for(int i=0; i<list.size(); i++) {
	       System.out.println("Stored string in redis:: "+list.get(i));
	     }
		jedis.close();
	}
}
