package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import rzy.core.Dao;
import rzy.core.Where;
import rzy.util.TimeUtil;

public class TopicService
{
	public Map<String, Object> list(Map<String, Object> map)
	{
		int total = 0;
		List<Map<String, Object>> data = null;
		Map<String, Object> result = new HashMap<String, Object>();
		String sql1 = "select count(*) from topic";
		String sql2 = "select t.*,(select count(id) from reply r where r.tid = t.id) as replies from topic t";
		Where where = Where.create();
		if (map != null && !map.isEmpty())
		{
			Object title = map.get("title");
			Object memo = map.get("memo");
			Object scope = map.get("scope");
			Object username = map.get("username");
			if (StringUtils.isNotBlank(ObjectUtils.toString(title)))
			{
				where.add("title", "like", title.toString().trim());
			}
			if (StringUtils.isNotBlank(ObjectUtils.toString(memo)) && !("-1").equals(memo))
			{
				where.add("memo", "=", memo);
			}
			if ("1".equals(scope))
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
			where.orderby("toptime desc,updatetime desc,id desc");
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
		String sql = "insert into topic(id,title,memo,content,creator,createtime,updatetime,toptime) values(?,?,?,?,?,?,?,?)";
		int id = Dao.getID("topic");
		Object title = map.get("title");
		Object memo = map.get("memo");
		Object content = map.get("content");
		Object creator = map.get("creator");
		String time = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { id, title, memo, content, creator, time, time, time };
		Dao.update(sql, params);
	}

	public void del(int id)
	{
		String sql = "delete from topic where id=?";
		Dao.update(sql, new Object[] { id });
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

	public List<Map<String, Object>> export()
	{
		String sql = "select * from topic t";
		List<Map<String, Object>> list = Dao.find(sql);
		return list;
	}
}
