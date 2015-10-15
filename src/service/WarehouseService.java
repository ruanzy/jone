package service;

import java.util.List;
import java.util.Map;
import com.rz.common.Record;
import com.rz.dao.DB;
import com.rz.dao.SQLMapper;
import com.rz.tx.Transaction;

public class WarehouseService
{
	private DB dao = DB.getInstance();

//	public Pager list(Map<String, String> map)
//	{
//		String sqlid1 = "warehouse.count";
//		String sqlid2 = "warehouse.selectAll";
//		Pager pager = SQLMapper.pager(sqlid1, sqlid2, map);
//		return pager;
//	}
	
	public List<Record> list(Map<String, String> map)
	{
		return SQLMapper.find("warehouse.selectAll", map);
	}

	public void add(Map<String, String> map)
	{
		String sql = "insert into warehouse(id,code,name,state,memo) values(?,?,?,?,?)";
		int id = dao.getID("warehouse");
		Object name = map.get("name");
		Object code = map.get("code");
		Object memo = map.get("memo");
		Object state = map.get("state");
		Object[] params = new Object[] { id, code, name, state, memo };
		dao.update(sql, params);
	}

	@Transaction
	public void del(String ids)
	{
		Object[] arr = ids.split(",");
		StringBuffer sql2 = new StringBuffer("delete from warehouse where id in (");
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

	public void mod(Map<String, Object> map)
	{
		String sql = "update warehouse set name=?,state=? where id=?";
		Object name = map.get("name");
		Object state = map.get("state");
		Object id = map.get("id");
		Object[] params = new Object[] { name, state, id };
		dao.update(sql, params);
	}
}
