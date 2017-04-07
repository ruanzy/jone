package com.rz.data.mongo;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.rz.data.DataAccessException;

public final class MongoUtil
{
	static MongoClient client = null;
	static MongoDatabase db = null;
	static
	{
		InputStream is = null;
		Properties prop = new Properties();
		try
		{
			is = MongoUtil.class.getClassLoader().getResourceAsStream("mongo.properties");
			if (is == null)
			{
				is = new FileInputStream("mongo.properties");
			}
			prop.load(is);
			String ip = prop.getProperty("mongo.ip", "127.0.0.1");
			int port = Integer.valueOf(prop.getProperty("mongo.port", "27017"));
			client = new MongoClient(ip, port);
		}
		catch (Exception e)
		{
			throw new DataAccessException("Create Mongo DataSource Exception!", e);
		}
	}

	public static MongoClient getClient()
	{
		return client;
	}
}