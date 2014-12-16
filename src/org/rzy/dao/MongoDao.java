package org.rzy.dao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;

public final class MongoDao
{
	static DB db = null;
	static
	{
		InputStream is = null;
		Properties prop = new Properties();
		try
		{
			is = MongoDao.class.getClassLoader().getResourceAsStream("dao.properties");
			if (is == null)
			{
				is = new FileInputStream("dao.properties");
			}
			prop.load(is);
			String ip = prop.getProperty("mongo.ip", "127.0.0.1");
			Mongo mongo = new Mongo(ip);
			String dbname = prop.getProperty("mongo.db");
			db = mongo.getDB(dbname);
		}
		catch (Exception e)
		{
			throw new DataAccessException("Create Mongo DataSource Exception!", e);
		}
	}

	private static DBCollection getColl(String collname)
	{
		try
		{
			return db.getCollection(collname);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	public static int insert(String collection, DBObject... arr)
	{
		return getColl(collection).insert(arr).getN();
	}

	public static int delete(String collection, DBObject obj)
	{
		return getColl(collection).remove(obj).getN();
	}

	public static int update(String collection, DBObject o1, DBObject o2)
	{
		return getColl(collection).update(o1, o2, true, true).getN();
	}

	public static DBObject findOne(String collection, DBObject query, int skip, int limit)
	{
		return getColl(collection).findOne(query);
	}

	public static List<DBObject> find(String collection, DBObject query, int skip, int limit)
	{
		return getColl(collection).find(query).skip(skip).limit(limit).toArray();
	}

	public static List<DBObject> find(String collection, DBObject query)
	{
		return getColl(collection).find(query).toArray();
	}

	public static long count(String collection, DBObject query)
	{
		long result = 0;
		try
		{
			result = getColl(collection).count(query);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e.getCause().getMessage(), e);
		}
		return result;
	}

	public DBCursor MapReduce(String collection, DBObject query, String map, String reduce, String resultCollection)
	{
		MapReduceOutput mapReduceOutput = getColl(collection).mapReduce(map, reduce, resultCollection, query);
		DBCollection resultColl = mapReduceOutput.getOutputCollection();
		DBCursor cursor = resultColl.find();
		return cursor;
	}
}