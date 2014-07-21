package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import rzy.core.Dao;
import rzy.core.Where;
import rzy.util.MD5Util;
import rzy.util.TimeUtil;

public class PmsService
{
	private Dao dao = Dao.getInstance();
	
	public Map<String, Object> finduser(Map<String, Object> map)
	{
		int total = 0;
		List<Map<String, Object>> data = null;
		Map<String, Object> result = new HashMap<String, Object>();
		String sql1 = "select count(*) from users";
		String sql2 = "select * from users";
		Where where = Where.create();
		where.add("id", ">", -1);
		if (map != null && !map.isEmpty())
		{
			Object username = map.get("username");
			Object state = map.get("state");
			if (StringUtils.isNotBlank(ObjectUtils.toString(username)))
			{
				where.add("username", "like", username.toString().trim());
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(state)))
			{
				where.add("state", "=", state);
			}
		}
		Object scalar = dao.scalar(where.appendTo(sql1));
		if (scalar != null)
		{
			total = Integer.valueOf(scalar.toString());
		}
		result.put("total", total);
		if (total > 0)
		{
			where.orderby("id desc");
			int page = Integer.valueOf(map.get("page").toString());
			int pagesize = Integer.valueOf(map.get("pagesize").toString());
			int pagecount = (total % pagesize == 0) ? (total / pagesize) : (total / pagesize + 1);
			if (pagecount > 0)
			{
				page = (pagecount < page) ? pagecount : page;
			}
			data = dao.pager(where.appendTo(sql2), page, pagesize);
			result.put("page", page);
			result.put("data", data);
		}
		return result;
	}

	public void adduser(Map<String, Object> map)
	{
		String sql = "insert into topic(id,title,memo,content,creator,createtime,updatetime,toptime) values(?,?,?,?,?,?,?,?)";
		int id = dao.getID("topic");
		Object title = map.get("title");
		Object memo = map.get("memo");
		Object content = map.get("content");
		Object creator = map.get("creator");
		String time = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { id, title, memo, content, creator, time, time, time };
		dao.update(sql, params);
	}

	public void deluser(String ids)
	{
		String[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from userrole where userid in (");
		for (int k = 0, len = arr.length; k < len; k++)
		{
			sql1.append("?");
			if (k != len - 1)
			{
				sql1.append(",");
			}
		}
		sql1.append(")");
		dao.update(sql1.toString(), arr);
		StringBuffer sql2 = new StringBuffer("delete from users where id in (");
		for (int k = 0, len = arr.length; k < len; k++)
		{
			sql2.append("?");
			if (k != len - 1)
			{
				sql2.append(",");
			}
		}
		sql2.append(")");
		dao.update(sql2.toString(), arr);
	}

	public void moduser(Map<String, Object> map)
	{
		String sql = "update topic set title=?,memo=?,content=?,updatetime=? where id=?";
		Object title = map.get("title");
		Object memo = map.get("memo");
		Object content = map.get("content");
		String updatetime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object id = map.get("id");
		Object[] params = new Object[] { title, memo, content, updatetime, id };
		dao.update(sql, params);
	}

	public void activeuser(String ids)
	{
		StringBuffer sql = new StringBuffer("update users set state=1 where id in(");
		String[] arr = ids.split(",");
		for (int k = 0, len = arr.length; k < len; k++)
		{
			sql.append("?");
			if (k != len - 1)
			{
				sql.append(",");
			}
		}
		sql.append(")");
		dao.update(sql.toString(), arr);
	}

	public void canceluser(String ids)
	{
		StringBuffer sql = new StringBuffer("update users set state=0 where id in(");
		String[] arr = ids.split(",");
		for (int k = 0, len = arr.length; k < len; k++)
		{
			sql.append("?");
			if (k != len - 1)
			{
				sql.append(",");
			}
		}
		sql.append(")");
		dao.update(sql.toString(), arr);
	}

	public Map<String, Object> getuser(String id)
	{
		Map<String, Object> map = null;
		String sql = "select * from topic where id=?";
		List<Map<String, Object>> list = dao.find(sql, new Object[] { id });
		if (list.size() == 1)
		{
			map = list.get(0);
		}
		return map;
	}

	public List<Map<String, Object>> getContents(String id)
	{
		String sql = "select * from reply where tid=? order by createtime desc";
		return dao.find(sql, new Object[] { id });
	}

	public Map<String, Object> login(Map<String, Object> map)
	{
		Map<String, Object> result = null;
		String sql = "select * from users where username=? and pwd=?";
		String username = (String) map.get("username");
		String password = (String) map.get("password");
		password = MD5Util.md5(username + password);
		List<Map<String, Object>> list = dao.find(sql, new Object[] { username, password });
		if (list.size() == 1)
		{
			result = list.get(0);
		}
		return result;
	}

	public void addContent(Map<String, Object> map)
	{
		String sql = "insert into reply(id,tid,content,creator,createtime) values(?,?,?,?,?)";
		int id = dao.getID("reply");
		Object tid = map.get("tid");
		Object content = map.get("content");
		Object creator = map.get("creator");
		String createtime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { id, tid, content, creator, createtime };
		dao.update(sql, params);
	}

	public void reg(Map<String, Object> map)
	{
		String sql = "insert into users(id,username,pwd,regtime) values(?,?,?,?)";
		int id = dao.getID("users");
		String username = (String) map.get("username");
		String pwd = (String) map.get("password");
		pwd = MD5Util.md5(username + pwd);
		String regtime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { id, username, pwd, regtime };
		dao.update(sql, params);
	}

	public void top(String id)
	{
		String sql = "update topic set toptime=? where id=?";
		String toptime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { toptime, id };
		dao.update(sql, params);
	}

	public List<Map<String, Object>> getTopics()
	{
		String sql = "select * from topic";
		return dao.find(sql);
	}

	public void delContent(String id)
	{
		String sql = "delete from reply where id=?";
		Object[] params = new Object[] { id };
		dao.update(sql, params);
	}

	public String userexist(String username)
	{
		String count = "0";
		String sql = "select count(*) from users where username=?";
		Object[] params = new Object[] { username };
		Object scalar = dao.scalar(sql, params);
		if (scalar != null)
		{
			count = scalar.toString();
		}
		return count;
	}

	public List<Map<String, Object>> getCatalogs()
	{
		String sql = "select * from catalog order by value";
		return dao.find(sql);
	}

	public Map<String, Object> findlog(Map<String, Object> map)
	{
		int total = 0;
		List<Map<String, Object>> data = null;
		Map<String, Object> result = new HashMap<String, Object>();
		String sql1 = "select count(*) from log";
		String sql2 = "select l.*,r.name as methodname from log l left join resources r on l.method=r.method";
		Where where = Where.create();
		if (map != null && !map.isEmpty())
		{
			Object operator = map.get("operator_text");
			Object time1 = map.get("time1");
			Object time2 = map.get("time2");
			if (StringUtils.isNotBlank(ObjectUtils.toString(operator)))
			{
				where.add("operator", "=", operator.toString().trim());
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(time1)))
			{
				where.add("time", ">=", time1);
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(time2)))
			{
				where.add("time", "<=", time2);
			}
		}
		Object scalar = dao.scalar(where.appendTo(sql1));
		if (scalar != null)
		{
			total = Integer.valueOf(scalar.toString());
		}
		result.put("total", total);
		if (total > 0)
		{
			where.orderby("l.id desc");
			int page = Integer.valueOf(map.get("page").toString());
			int pagesize = Integer.valueOf(map.get("pagesize").toString());
			int pagecount = (total % pagesize == 0) ? (total / pagesize) : (total / pagesize + 1);
			if (pagecount > 0)
			{
				page = (pagecount < page) ? pagecount : page;
			}
			data = dao.pager(where.appendTo(sql2), page, pagesize);
			result.put("page", page);
			result.put("data", data);
		}
		return result;
	}

	public void addcatalog(Map<String, Object> map)
	{
		String sql = "insert into catalog(text,value) values(?,?)";
		Object text = map.get("text");
		Object value = map.get("value");
		Object[] params = new Object[] { text, value };
		dao.update(sql, params);
	}

	public void addBatchcatalog(List<Map<String, Object>> list)
	{
		String sql = "insert into catalog(text,value) values(?,?)";
		List<Object[]> params = new ArrayList<Object[]>();
		for (Map<String, Object> map2 : list)
		{

			Object text = map2.get("text");
			Object value = map2.get("value");
			params.add(new Object[] { text, value });
		}
		dao.beginBatch(sql);
		for (Map<String, Object> map2 : list)
		{
			Object text = map2.get("text");
			Object value = map2.get("value");
			dao.addBatch(new Object[] { text, value });
		}
		dao.excuteBatch();
		dao.endBatch();
	}

	public void addres(Map<String, Object> map)
	{
		String sql = "insert into resources(id,name,url,icon,type,pid,path,method,flag) values(?,?,?,?,?,?,?,?,?)";
		int id = dao.getID("resources");
		Object type = map.get("type");
		Object url = map.get("url");
		Object name = map.get("name");
		Object method = map.get("method");
		String pid = String.valueOf(map.get("pid"));
		Object icon = map.get("icon");
		String path = String.valueOf(id);
		if (!"1".equals(type))
		{
			path = getParentPath(pid) + "/" + id;
		}
		Object[] params = new Object[] { id, name, url, icon, type, pid, path, method, 1 };
		dao.update(sql, params);
	}

	private String getParentPath(String pid)
	{
		String sql = "select path from resources where id = ?";
		Object[] params = new Object[] { pid };
		String path = (String) dao.scalar(sql, params);
		return path;
	}

	public List<Map<String, Object>> getRes()
	{
		String sql = "select r.*,'true' as open from resources r where r.id>=100";
		return dao.find(sql);
	}

	public List<Map<String, Object>> menubymoudle(String pid)
	{
		String sql = "select * from resources where pid=?";
		Object[] params = new Object[] { pid };
		return dao.find(sql, params);
	}

	public Map<String, Object> finddic(Map<String, Object> map)
	{
		int total = 0;
		List<Map<String, Object>> data = null;
		Map<String, Object> result = new HashMap<String, Object>();
		String sql1 = "select count(*) from dic";
		String sql2 = "select * from dic";
		Where where = Where.create();
		if (map != null && !map.isEmpty())
		{
			Object type = map.get("type");
			if (StringUtils.isNotBlank(ObjectUtils.toString(type)))
			{
				where.add("type", "like", type.toString().trim());
			}
		}
		Object scalar = dao.scalar(where.appendTo(sql1));
		if (scalar != null)
		{
			total = Integer.valueOf(scalar.toString());
		}
		result.put("total", total);
		if (total > 0)
		{
			where.orderby("id desc");
			int page = Integer.valueOf(map.get("page").toString());
			int pagesize = Integer.valueOf(map.get("pagesize").toString());
			int pagecount = (total % pagesize == 0) ? (total / pagesize) : (total / pagesize + 1);
			if (pagecount > 0)
			{
				page = (pagecount < page) ? pagecount : page;
			}
			data = dao.pager(where.appendTo(sql2), page, pagesize);
			result.put("page", page);
			result.put("data", data);
		}
		return result;
	}

	public void adddic(Map<String, Object> map)
	{
		String sql = "insert into dic(id,name,val,type,memo) values(?,?,?,?,?)";
		int id = dao.getID("dic");
		Object type = map.get("type");
		Object val = map.get("val");
		Object name = map.get("name");
		Object memo = map.get("memo");
		Object[] params = new Object[] { id, name, val, type, memo };
		dao.update(sql, params);
	}

	public void deldic(String ids)
	{
		StringBuffer sql = new StringBuffer("delete from dic where id in(");
		String[] arr = ids.split(",");
		for (int k = 0, len = arr.length; k < len; k++)
		{
			sql.append("?");
			if (k != len - 1)
			{
				sql.append(",");
			}
		}
		sql.append(")");
		dao.update(sql.toString(), arr);
	}

	public void moddic(Map<String, Object> map)
	{
		String sql = "update dic set name=?,val=?,type=? where id=?";
		Object id = map.get("id");
		Object type = map.get("type");
		Object val = map.get("val");
		Object name = map.get("name");
		Object[] params = new Object[] { name, val, type, id };
		dao.update(sql, params);
	}

	public Object menu()
	{
		String sql = "select * from resources where type in(1,2)";
		return dao.find(sql);
	}

	public Map<String, Object> findrole(Map<String, Object> map)
	{
		int total = 0;
		List<Map<String, Object>> data = null;
		Map<String, Object> result = new HashMap<String, Object>();
		String sql1 = "select count(*) from role";
		String sql2 = "select * from role";
		Where where = Where.create();
		if (map != null && !map.isEmpty())
		{
			Object name = map.get("name");
			if (StringUtils.isNotBlank(ObjectUtils.toString(name)))
			{
				where.add("name", "like", name.toString().trim());
			}
		}
		Object scalar = dao.scalar(where.appendTo(sql1));
		if (scalar != null)
		{
			total = Integer.valueOf(scalar.toString());
		}
		result.put("total", total);
		if (total > 0)
		{
			where.orderby("id desc");
			int page = Integer.valueOf(map.get("page").toString());
			int pagesize = Integer.valueOf(map.get("pagesize").toString());
			int pagecount = (total % pagesize == 0) ? (total / pagesize) : (total / pagesize + 1);
			if (pagecount > 0)
			{
				page = (pagecount < page) ? pagecount : page;
			}
			data = dao.pager(where.appendTo(sql2), page, pagesize);
			result.put("page", page);
			result.put("data", data);
		}
		return result;
	}

	public void addrole(Map<String, Object> map)
	{
		String sql = "insert into role(id,name) values(?,?)";
		int id = dao.getID("role");
		Object name = map.get("name");
		Object[] params = new Object[] { id, name };
		dao.update(sql, params);
	}

	public void setres(String role, String res)
	{
		String sql1 = "delete from roleres where roleid=?";
		dao.update(sql1, new Object[] { role });
		if (StringUtils.isNotBlank(res))
		{
			String sql2 = "insert into roleres(roleid,resid) values(?,?)";
			String[] arr = res.split(",");
			for (String r : arr)
			{
				Object[] params = new Object[] { role, r };
				dao.update(sql2, params);
			}
		}
	}

	public List<Map<String, Object>> allrole()
	{
		String sql = "select * from role";
		return dao.find(sql);
	}

	public void setrole(String user, String roles)
	{
		String sql1 = "delete from userrole where userid=?";
		dao.update(sql1, new Object[] { user });
		if (StringUtils.isNotBlank(roles))
		{
			String sql2 = "insert into userrole(userid,roleid) values(?,?)";
			String[] arr = roles.split(",");
			dao.beginBatch(sql2);
			for (String r : arr)
			{
				dao.addBatch(new Object[] { user, r });
			}
			dao.excuteBatch();
			dao.endBatch();
		}
	}

	public List<String> assignedroles(String user)
	{
		List<String> ret = new ArrayList<String>();
		String sql = "select roleid from userrole where userid=?";
		List<Map<String, Object>> list = dao.find(sql, new Object[] { user });
		for (Map<String, Object> map : list)
		{
			ret.add(String.valueOf(map.get("roleid")));
		}
		return ret;
	}

	public Object roleres(String role)
	{
		List<String> ret = new ArrayList<String>();
		String sql = "select resid from roleres where roleid=?";
		List<Map<String, Object>> list = dao.find(sql, new Object[] { role });
		for (Map<String, Object> map : list)
		{
			ret.add(String.valueOf(map.get("resid")));
		}
		return ret;
	}

	public void delres(String id)
	{
		String sql1 = "delete from roleres where resid in(select id from resources where path like '%" + id + "%')";
		String sql = "delete from resources where path like '%" + id + "%'";
		dao.update(sql1);
		dao.update(sql);
	}

	public void delroles(String id)
	{
		String sql1 = "delete from roleres where roleid=" + id;
		String sql2 = "delete from userrole where roleid=" + id;
		String sql3 = "delete from role where id=" + id;
		dao.update(sql1);
		dao.update(sql2);
		dao.update(sql3);
	}

	public Map<String, List<Map<String, Object>>> userres(String user)
	{
		Map<String, List<Map<String, Object>>> ret = new HashMap<String, List<Map<String, Object>>>();
		List<Map<String, Object>> menus = null;
		List<Map<String, Object>> ops = null;
		List<Map<String, Object>> res = null;
		if ("-1".equals(user))
		{
			String sql1 = "select * from resources";
			res = dao.find(sql1);
		}
		else
		{
			String sql2 = "select * from resources where id in(select distinct resid from roleres where roleid in (select roleid from userrole where userid=?))";
			Object[] params = new Object[] { user };
			res = dao.find(sql2, params);
		}
		if (res != null)
		{
			menus = new ArrayList<Map<String, Object>>();
			ops = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : res)
			{
				String type = String.valueOf(map.get("type"));
				if ("3".equals(type))
				{
					ops.add(map);
				}
				else
				{
					menus.add(map);
				}
			}
		}
		ret.put("menus", menus);
		ret.put("ops", ops);
		return ret;
	}

	public List<Map<String, Object>> dics()
	{

		String sql = "select * from dic";
		return dao.find(sql);
	}

	public List<Map<String, Object>> res()
	{
		String sql = "select * from resources";
		return dao.find(sql);
	}

	public List<Map<String, Object>> alluser()
	{
		String sql = "select u.id as value, u.username as text from users u";
		return dao.find(sql);
	}

	public List<Map<String, Object>> test(Map<String, Object> p, int page, int pagesize)
	{
		String sql = "select * from users";
		return dao.pager(sql, page, pagesize);
	}
}
