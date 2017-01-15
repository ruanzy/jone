package com.rz.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.rz.common.R;
import com.rz.dao.DB;
import com.rz.util.Base64Util;

public class Privilege
{
	static DB _db;

	public static void init(DB db)
	{
		InputStream is = null;
		try
		{
			_db = db;
			String fileName = "jone.sql";
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/rz/web/" + fileName);
			_db.runScript(new InputStreamReader(is, "UTF8"));
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			try
			{
				if (is != null)
				{
					is.close();
				}
			}
			catch (IOException e)
			{

			}
		}
	}

	public static class User
	{
		public static List<R> findAll()
		{
			String sql = "select * from users";
			return _db.find(sql);
		}

		public static void add(R r)
		{
			String userid = r.getString("userid");
			String password = r.getString("password");
			String _password = Base64Util.encode(userid + password);
			String email = r.getString("email");
			String phone = r.getString("phone");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String regtime = df.format(new Date());
			String memo = r.getString("memo");
			String sql = "insert into users values(?,?,0,?,?,1,?,?)";
			_db.begin();
			_db.update(sql, new Object[] { userid, _password, email, phone, regtime, memo });
			_db.close();
		}

		public static void delete(String userid)
		{
			String sql = "delete from users where userid=?";
			_db.begin();
			_db.update(sql, new Object[] { userid });
			_db.close();
		}

		public static void update(String userid, R r)
		{
			String password = r.getString("password");
			String _password = Base64Util.encode(userid + password);
			String email = r.getString("email");
			String phone = r.getString("phone");
			String memo = r.getString("memo");
			String sql = "update users set password=?,email=?,password=?,phone=?,memo=? where userid=?";
			_db.begin();
			_db.update(sql, new Object[] { _password, email, phone, memo, userid });
			_db.close();
		}

		public static void setRoles(String userid, List<Integer> roleids)
		{
			String sql1 = "delete from userrole where userid = ?";
			String sql2 = "insert into userrole values(?, ?)";
			_db.begin();
			_db.update(sql1, new Object[] { userid });
			for (Integer roleid : roleids)
			{
				_db.update(sql2, new Object[] { userid, roleid });
			}
			_db.close();
		}

		public static List<R> getRoles(String userid)
		{
			String sql = "select r.id, r.name, exists (select 1 from userrole where userid=? and roleid=r.id) as checked from role r";
			return _db.find(sql, userid);
		}
	}

	public static class Role
	{
		public List<R> findAll()
		{
			String sql = "select * from role";
			return _db.find(sql);
		}

		public static void add(String rolename)
		{
			String sql = "insert into role values(?)";
			_db.begin();
			_db.update(sql, new Object[] { rolename });
			_db.close();
		}

		public void setResources(String roleid, List<Integer> resourceids)
		{
			String sql1 = "delete from roleres where roleid = ?";
			String sql2 = "insert into roleres values(?, ?)";
			_db.begin();
			_db.update(sql1, new Object[] { roleid });
			for (Integer resourceid : resourceids)
			{
				_db.update(sql2, new Object[] { roleid, resourceid });
			}
			_db.close();
		}

		public List<R> getResources(String roleid)
		{
			String sql = "select r.*, exists (select 1 from roleres where roleid=? and resid=r.id) as checked from resources r";
			return _db.find(sql, roleid);
		}

		public static class Resource
		{
			public List<R> findAll()
			{
				String sql = "select * from resources";
				return _db.find(sql);
			}

			public static void add(R r)
			{
				int id = r.getInt("id");
				String name = r.getString("name");
				String type = r.getString("type");
				String method = r.getString("method");
				int pid = r.getInt("pid");
				String url = r.getString("url");
				String iconcls = r.getString("iconcls");
				String path = r.getString("path");
				String flag = r.getString("flag");
				String sql = "insert into resources values(?,?,?,?,?,?,?,?,?)";
				_db.begin();
				_db.update(sql, new Object[] { id, name, type, method, pid, url, iconcls, path, flag });
				_db.close();
			}
		}
	}
}
