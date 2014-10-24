package org.rzy.util;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class JSONUtil
{

	public static String toJSONString(Object obj)
	{
		return JSON.toJSONString(obj);
	}

	public static List<Map<String, Object>> toList(String str)
	{
		List<Map<String, Object>> data = null;
		try
		{
			data = JSON.parseObject(str, new TypeReference<List<Map<String, Object>>>()
			{
			});
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return data;
	}
}
