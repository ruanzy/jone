package service;

import java.util.List;
import java.util.Map;
import com.rz.common.Record;
import com.rz.dao.Dao;
import com.rz.dao.SQLMapper;
import com.rz.sql.Pager;

public class DepartService
{
	private Dao dao = Dao.getInstance();

	public List<Record> tree()
	{
		String sql = "select * from depart";
		return dao.find(sql);
	}
	
	public Pager find(Map<String, String> map)
	{
		String sqlid2 = "depart.selectAll";
		Pager pager = SQLMapper.pager(sqlid2, map);
		return pager;
	}
	
	public void add(Map<String, Object> map)
	{
		String sql = "insert into depart(id,name,pid,isParent,memo) values(?,?,?,?,?)";
		int id = dao.getID("depart");
		Object name = map.get("name");
		Object pid = map.get("pid");
		Object isParent = map.get("isParent");
		Object memo = map.get("memo");
		Object[] params = new Object[] { id, name, pid, isParent, memo };
		dao.update(sql, params);
	}
	
	public void del(String ids)
	{
		Object[] arr = ids.split(",");
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
