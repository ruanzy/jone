package jone.data.db.sql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jone.R;
import jone.data.db.DB;

public class SQLExecutor
{
	DB db;

	public SQLExecutor(DB db)
	{
		this.db = db;
	}

	public int update(String sqlid, Map<String, Object> params)
	{
		SqlPara _sql = SQLLoader.getSql(sqlid, params);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.update(sql, _params);
	}
	
	public int update(String sqlid, R params)
	{
		SqlPara _sql = SQLLoader.getSql(sqlid, params);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.update(sql, _params);
	}

	public List<R> find(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		SqlPara _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.find(sql, _params);
	}
	
	public List<R> find(String sqlid, R params)
	{
		SqlPara _sql = SQLLoader.getSql(sqlid, params);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.find(sql, _params);
	}
	
	public ResultSet findBigData(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		SqlPara _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.findBigData(sql, _params);
	}

	public R findOne(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		SqlPara _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.findOne(sql, _params);
	}
	
	public R findOne(String sqlid, R params)
	{
		SqlPara _sql = SQLLoader.getSql(sqlid, params);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.findOne(sql, _params);
	}

	public Object scalar(String sqlid, Map<String, String> params)
	{
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		SqlPara _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.scalar(sql, _params);
	}
	
	public Object scalar(String sqlid, R params)
	{
		SqlPara _sql = SQLLoader.getSql(sqlid, params);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.scalar(sql, _params);
	}

	public List<R> pager(String sqlid, Map<String, String> params, int page, int pagesize)
	{
		Map<String, Object> p = new HashMap<String, Object>();
		if (null != params)
		{
			p.putAll(params);
		}
		SqlPara _sql = SQLLoader.getSql(sqlid, p);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.pager(sql, _params, page, pagesize);
	}
	
	public List<R> pager(String sqlid, R params, int page, int pagesize)
	{
		SqlPara _sql = SQLLoader.getSql(sqlid, params);
		String sql = _sql.getSql();
		Object[] _params = _sql.getPara();
		return db.pager(sql, _params, page, pagesize);
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
		SqlPara _countsql = SQLLoader.getSql(countsql, p);
		String sql1 = _countsql.getSql();
		Object[] params1 = _countsql.getPara();
		//log.debug(sql);
		Object count = db.scalar(sql1, params1);
		long total = Long.parseLong(count.toString());
		if (total > 0)
		{
			SqlPara _pagesql = SQLLoader.getSql(pagesql, p);
			String sql2 = _pagesql.getSql();
			Object[] params2 = _pagesql.getPara();
			//log.debug(sql2);
			data = db.pager(sql2, params2, page, pagesize);
		}
		r.put("total", total);
		r.put("data", data);
		return r;
	}
	
	public R pager(String countsql, String pagesql, R params, int page, int pagesize)
	{
		R r = new R();
		List<R> data = new ArrayList<R>();
		SqlPara _countsql = SQLLoader.getSql(countsql, params);
		String sql1 = _countsql.getSql();
		Object[] params1 = _countsql.getPara();
		//log.debug(sql);
		Object count = db.scalar(sql1, params1);
		long total = Long.parseLong(count.toString());
		if (total > 0)
		{
			SqlPara _pagesql = SQLLoader.getSql(pagesql, params);
			String sql2 = _pagesql.getSql();
			Object[] params2 = _pagesql.getPara();
			//log.debug(sql2);
			data = db.pager(sql2, params2, page, pagesize);
		}
		r.put("total", total);
		r.put("data", data);
		return r;
	}
}
