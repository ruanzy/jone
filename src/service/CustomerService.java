package service;

import java.util.List;
import java.util.Map;
import com.rz.common.Record;
import com.rz.dao.Dao;
import com.rz.dao.Pager;
import com.rz.dao.SQLMapper;
import com.rz.tx.Transaction;

public class CustomerService
{
	private Dao dao = Dao.getInstance();
	
	public List<Record> category()
	{
		String sql = "select * from customer_category";
		return dao.find(sql);
	}
	
	public void categoryadd(Map<String, String> map)
	{
		String sql = "insert into goods_category(id,name) values(?,?)";
		int id = dao.getID("goods_category");
		Object name = map.get("name");
		Object[] params = new Object[] { id, name };
		dao.update(sql, params);
	}

	@Transaction
	public void categorydel(String ids)
	{
		String[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from goods_category where id in (");
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

	public void categorymod(Map<String, Object> map)
	{
		String sql = "update goods_category set name=? where id=?";
		Object name = map.get("name");
		Object id = map.get("id");
		Object[] params = new Object[] { name, id };
		dao.update(sql, params);
	}

	public List<Record> list(Map<String, String> map)
	{
		String sql = "select * from customer";
		return dao.find(sql);
	}
	
	public Pager pager(Map<String, String> map)
	{
		String sqlid1 = "customer.count";
		String sqlid2 = "customer.selectAll";
		Pager pager = SQLMapper.pager(sqlid1, sqlid2, map);
		return pager;
	}

	public void add(Map<String, String> map)
	{
		String sql = "insert into customer(id,name,phone) values(?,?,?)";
		int id = dao.getID("customer");
		Object name = map.get("name");
		Object phone = map.get("phone");
		Object[] params = new Object[] { id, name, phone };
		dao.update(sql, params);
	}

	@Transaction
	public void del(String ids)
	{
		String[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from customer where id in (");
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
		String sql = "update customer set name=?,phone=? where id=?";
		Object name = map.get("name");
		Object phone = map.get("phone");
		Object id = map.get("id");
		Object[] params = new Object[] { name, phone, id };
		dao.update(sql, params);
	}
}
