package service;

import java.util.List;
import java.util.Map;
import com.rz.dao.Dao;
import com.rz.tx.Transaction;

public class WarehouseService
{
	private Dao dao = Dao.getInstance();

//	public Pager list(Map<String, String> map)
//	{
//		String sqlid1 = "warehouse.count";
//		String sqlid2 = "warehouse.selectAll";
//		Pager pager = SQLMapper.pager(sqlid1, sqlid2, map);
//		return pager;
//	}
	
	public List<Map<String,Object>> list(Map<String, String> map)
	{
		String sql = "select * from warehouse";
		return dao.find(sql);
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
		String[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from warehouse where userid in (");
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
		String sql = "update warehouse set username=?,depart=?,memo=?,phone=?,email=?,gender=?,state=? where id=?";
		Object username = map.get("username");
		Object depart = map.get("depart");
		Object memo = map.get("memo");
		Object phone = map.get("phone");
		Object email = map.get("email");
		Object gender = map.get("gender");
		Object state = map.get("state");
		Object id = map.get("id");
		Object[] params = new Object[] { username, depart, memo, phone, email, gender, state, id };
		dao.update(sql, params);
	}
}
