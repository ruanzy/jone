package com.rz.data.db;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import com.rz.data.DataAccessException;
import com.rz.util.Config;

@SuppressWarnings("unchecked")
public class DBs
{
	static Map<String, DB> dbs = new HashMap<String, DB>();
	static Map<String, Properties> dbp = new HashMap<String, Properties>();
	static
	{
		try
		{
			Object o = Config.get("datasource");
			if(o != null){
				Map<String, Object> datasource = (Map<String, Object>) o;
				for (Map.Entry<String, Object> entry : datasource.entrySet())
				{
					String dsName = entry.getKey();
					Map<String, Object> v = (Map<String, Object>) entry.getValue();
					Properties p = new Properties();
					p.putAll(v);
					dbp.put(dsName, p);
					DataSource ds = BasicDataSourceFactory.createDataSource(p);
					DB db = new DB(ds);
					dbs.put(dsName, db);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new DataAccessException("Create DataSource Exception!", e);
		}
	}

	public static Properties getProperties(String dsName)
	{
		return dbp.get(dsName);
	}

	public static DB getDB(String dsName)
	{
		if (dbs.containsKey(dsName))
		{
			return dbs.get(dsName);
		}
		return null;
	}

	public static DB createDB(String dsName, Properties prop)
	{
		if (!dbs.containsKey(dsName))
		{
			try
			{
				dbp.put(dsName, prop);
				DataSource ds = BasicDataSourceFactory.createDataSource(prop);
				DB db = new DB(ds);
				dbs.put(dsName, db);
				return db;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args)
	{
		DB db = getDB("hsqldb");
		System.out.println(db);
	}
}