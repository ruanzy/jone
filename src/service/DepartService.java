package service;

import java.util.Map;
import org.rzy.dao.Dao;
import org.rzy.dao.Pager;
import org.rzy.dao.SQLMapper;

public class DepartService
{
	private Dao dao = Dao.getInstance();

	public Pager find(Map<String, Object> map)
	{
		String sqlid1 = "depart.count";
		String sqlid2 = "depart.selectAll";
		Pager pager = SQLMapper.pager(sqlid1, sqlid2, map);
		return pager;
	}
	
	public void add(Map<String, Object> map)
	{
		String sql = "insert into depart(id,name,memo) values(?,?,?)";
		int id = dao.getID("depart");
		Object name = map.get("name");
		Object memo = map.get("memo");
		Object[] params = new Object[] { id, name, memo };
		dao.update(sql, params);
	}
	
	public void del(String ids)
	{
		String[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from depart where id in (");
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
	}
}
