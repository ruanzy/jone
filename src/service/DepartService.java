package service;

import java.util.List;
import java.util.Map;
import org.rzy.dao.Dao;
import org.rzy.dao.Pager;
import org.rzy.dao.SQLMapper;

public class DepartService
{
	private Dao dao = Dao.getInstance();

	public List<Map<String,Object>> tree()
	{
		String sql = "select * from depart";
		return dao.find(sql);
	}
	
	public Pager find(Map<String, Object> map)
	{
		String sqlid1 = "depart.count";
		String sqlid2 = "depart.selectAll";
		Pager pager = SQLMapper.pager(sqlid1, sqlid2, map);
		return pager;
	}
	
	public void add(Map<String, Object> map)
	{
		String sql = "insert into depart(id,name,pid,memo) values(?,?,?,?)";
		int id = dao.getID("depart");
		Object name = map.get("name");
		Object pid = map.get("pid");
		Object memo = map.get("memo");
		Object[] params = new Object[] { id, name, pid, memo };
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
