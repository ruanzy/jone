package service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.rzy.dao.Dao;
import org.rzy.util.Where;

public class UserService
{
	private Dao dao = Dao.getInstance();
	
	public Map<String, Object> find(Map<String, Object> map)
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
			int page = 1;
			int pagesize = 10;
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
}
