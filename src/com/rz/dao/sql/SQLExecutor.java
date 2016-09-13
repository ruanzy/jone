package com.rz.dao.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.common.Record;
import com.rz.dao.DB;

public class SQLExecutor
{
	static Logger log = LoggerFactory.getLogger(SQLExecutor.class);
	DB db;

	public SQLExecutor(DB db)
	{
		this.db = db;
	}

	public int update(String sqlid, Map<String, Object> params)
	{
		Sql _sql = SQLLoader.getSql(sqlid, params);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.update(sql, _params.toArray());
	}

	public List<Record> find(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>(params);
		Sql _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.find(sql, _params.toArray());
	}

	public Record findOne(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>(params);
		Sql _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.findOne(sql, _params.toArray());
	}

	public Object scalar(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>(params);
		Sql _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.scalar(sql, _params.toArray());
	}

	public List<Record> pager(String sqlid, Map<String, String> params, int page, int pagesize)
	{
		Map<String, Object> p = new HashMap<String, Object>(params);
		Sql _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.pager(sql, _params.toArray(), page, pagesize);
	}
}
