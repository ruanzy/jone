package com.rz.dao.sql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.common.R;
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

	public List<R> find(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		Sql _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.find(sql, _params.toArray());
	}
	
	public ResultSet findBigData(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		Sql _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.findBigData(sql, _params.toArray());
	}

	public R findOne(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		Sql _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.findOne(sql, _params.toArray());
	}

	public Object scalar(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		Sql _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.scalar(sql, _params.toArray());
	}

	public List<R> pager(String sqlid, Map<String, String> params, int page, int pagesize)
	{
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		Sql _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		List<Object> _params = _sql.getParams();
		log.debug(sql);
		return db.pager(sql, _params.toArray(), page, pagesize);
	}

	public R pager(String countsql, String pagesql, Map<String, String> params, int page, int pagesize)
	{
		R r = new R();
		List<R> data = new ArrayList<R>();
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		Sql _countsql = SQLLoader.getSql(countsql, p);
		String sql1 = _countsql.getSql();
		List<Object> params1 = _countsql.getParams();
		//log.debug(sql);
		Object count = db.scalar(sql1, params1.toArray());
		long total = Long.parseLong(count.toString());
		if (total > 0)
		{
			Sql _pagesql = SQLLoader.getSql(pagesql, p);
			String sql2 = _pagesql.getSql();
			List<Object> params2 = _pagesql.getParams();
			//log.debug(sql2);
			data = db.pager(sql2, params2.toArray(), page, pagesize);
		}
		r.put("total", total);
		r.put("data", data);
		return r;
	}
}
