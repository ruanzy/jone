package com.rz.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;

public class DBPool {
	static Map<String, DB> dbs = new HashMap<String, DB>();
	static Map<String, Properties> dbProperties = new HashMap<String, Properties>();
	static {
		try {
			for (Map.Entry<String, Properties> entry : dbProperties.entrySet()) {
				String dsName = entry.getKey();
				Properties p = entry.getValue();
				dbProperties.put(dsName, p);
				DataSource ds = BasicDataSourceFactory.createDataSource(p);
				DB db = new DB(ds);
				dbs.put(dsName, db);
			}
		} catch (Exception e) {
			throw new DataAccessException("Create DataSource Exception!", e);
		}
	}
	
	public static Properties getDBProperties(String dsName){
		return dbProperties.get(dsName);
	}

	public static DB getDB(String dsName) {
		if (dbs.containsKey(dsName)) {
			return dbs.get(dsName);
		}
		return null;
	}
	
	public static DB createDB(String engine, String dsName, Properties prop) {
		if (!dbs.containsKey(engine + '_' + dsName)) {
			try {
				dbProperties.put(engine + '_' + dsName, prop);
				DataSource ds = BasicDataSourceFactory.createDataSource(prop);
				DB db = new DB(ds);
				dbs.put(dsName, db);
				return db;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}