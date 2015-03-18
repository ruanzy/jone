package service;

import java.util.List;
import java.util.Map;
import com.rz.dao.Dao;
import com.rz.tx.Transaction;

public class SupplierService
{
	private Dao dao = Dao.getInstance();
	
	public List<Map<String,Object>> category()
	{
		String sql = "select * from supplier_category";
		return dao.find(sql);
	}
	
	public List<Map<String,Object>> list()
	{
		String sql = "select * from supplier";
		return dao.find(sql);
	}

	public void add(Map<String, String> map)
	{
		String sql = "insert into supplier(id,name,category,phone) values(?,?,?,?)";
		int id = dao.getID("supplier");
		Object name = map.get("name");
		Object category = map.get("category");
		Object phone = map.get("phone");
		Object[] params = new Object[] { id, name, category, phone };
		dao.update(sql, params);
	}

	@Transaction
	public void del(String ids)
	{
		String[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from supplier where id in (");
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

	public void mod(Map<String, Object> map)
	{
		String sql = "update supplier set name=?,phone=? where id=?";
		Object name = map.get("name");
		Object phone = map.get("phone");
		Object id = map.get("id");
		Object[] params = new Object[] { name, phone, id };
		dao.update(sql, params);
	}
}
