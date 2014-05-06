package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import rzy.core.Dao;
import rzy.core.Where;
import rzy.util.TimeUtil;

public class ProjectService
{
	public Map<String, Object> list(Map<String, Object> map)
	{
		int total = 0;
		List<Map<String, Object>> data = null;
		Map<String, Object> result = new HashMap<String, Object>();
		String sql1 = "select count(*) from project";
		String sql2 = "select * from project";
		Where where = Where.create();
		where.add("id", ">", -1);
		if (map != null && !map.isEmpty())
		{
			Object name = map.get("name");
			Object username = map.get("username");
			if (StringUtils.isNotBlank(ObjectUtils.toString(name)))
			{
				where.add("name", "like", name.toString().trim());
			}
			if (username != null)
			{
				where.add("creator", "=", username);
			}
		}
		Object scalar = Dao.scalar(where.appendTo(sql1));
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
			data = Dao.pager(where.appendTo(sql2), page, pagesize);
			result.put("page", page);
			result.put("data", data);
		}
		return result;
	}

	public void add(Map<String, Object> map)
	{
		String sql = "insert into project(id,name,begintime,endtime,creator,createtime,memo) values(?,?,?,?,?,?,?)";
		int id = Dao.getID("project");
		Object name = map.get("name");
		Object begintime = map.get("begintime");
		Object endtime = map.get("endtime");
		Object creator = map.get("creator");
		Object memo = map.get("memo");
		String time = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { id, name, begintime, endtime, creator, time, memo };
		Dao.update(sql, params);
	}

	public void del(String ids)
	{
		String[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from project where id in (");
		for (int k = 0, len = arr.length; k < len; k++)
		{
			sql1.append("?");
			if (k != len - 1)
			{
				sql1.append(",");
			}
		}
		sql1.append(")");
		Dao.update(sql1.toString(), arr);
	}

	public void mod(Map<String, Object> map)
	{
		String sql = "update topic set title=?,memo=?,content=?,updatetime=? where id=?";
		Object title = map.get("title");
		Object memo = map.get("memo");
		Object content = map.get("content");
		String updatetime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object id = map.get("id");
		Object[] params = new Object[] { title, memo, content, updatetime, id };
		Dao.update(sql, params);
	}

	public Map<String, Object> get(String id)
	{
		Map<String, Object> map = null;
		String sql = "select * from topic where id=?";
		List<Map<String, Object>> list = Dao.find(sql, new Object[] { id });
		if (list.size() == 1)
		{
			map = list.get(0);
		}
		return map;
	}

	public List<Map<String, Object>> getContents(String id)
	{
		String sql = "select * from reply where tid=? order by createtime desc";
		return Dao.find(sql, new Object[] { id });
	}

	public void addContent(Map<String, Object> map)
	{
		String sql = "insert into reply(id,tid,content,creator,createtime) values(?,?,?,?,?)";
		int id = Dao.getID("reply");
		Object tid = map.get("tid");
		Object content = map.get("content");
		Object creator = map.get("creator");
		String createtime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { id, tid, content, creator, createtime };
		Dao.update(sql, params);
	}

	public void top(String id)
	{
		String sql = "update topic set toptime=? where id=?";
		String toptime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { toptime, id };
		Dao.update(sql, params);
	}

	public List<Map<String, Object>> getTopics()
	{
		String sql = "select * from topic";
		return Dao.find(sql);
	}

	public void delContent(String id)
	{
		String sql = "delete from reply where id=?";
		Object[] params = new Object[] { id };
		Dao.update(sql, params);
	}

	public List<Map<String, Object>> getRems(String id)
	{
		String sql = "select * from users where id>-1";
		return Dao.find(sql);
	}
}
