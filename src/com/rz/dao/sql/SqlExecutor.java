package com.rz.dao.sql;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rz.common.Record;
import com.rz.dao.DB;
import com.rz.dao.DBPool;

public class SqlExecutor {
	static Logger log = LoggerFactory.getLogger(SqlExecutor.class);
	static DB db = DBPool.getDB("hsqldb");
	
	public static void use(String ds) {
		db = DBPool.getDB(ds);
	}

	public static int update(String sqlid, Map<String, Object> params) {
		Sql _sql = SqlMapper.getSql(sqlid, params);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.update(sql, _params.toArray());
	}

	public static List<Record> find(String sqlid, Map<String, Object> params) {
		Sql _sql = SqlMapper.getSql(sqlid, params);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.find(sql, _params.toArray());
	}

	public static Record findOne(String sqlid, Map<String, Object> params) {
		Sql _sql = SqlMapper.getSql(sqlid, params);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.findOne(sql, _params.toArray());
	}
}
