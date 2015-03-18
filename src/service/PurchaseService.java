package service;

import java.util.List;
import java.util.Map;
import com.rz.dao.Dao;
import com.rz.dao.Pager;
import com.rz.dao.SQLMapper;
import com.rz.tx.Transaction;

public class PurchaseService
{
	private Dao dao = Dao.getInstance();

	public Pager list(Map<String, String> map)
	{
		String sqlid1 = "purchase.count";
		String sqlid2 = "purchase.selectAll";
		Pager pager = SQLMapper.pager(sqlid1, sqlid2, map);
		return pager;
	}
	
	public List<Map<String, Object>> detaillist(String pbill)
	{
		String sql = "select * from pbill_detail where pbill=?";
		Object[] params = new Object[] { pbill };
		return dao.find(sql, params);
	}

	public void add(Map<String, String> map)
	{
		String sql = "insert into purchase(id,name,category,unit,spec,purchase_price,sale_price) values(?,?,?,?,?,?,?)";
		int id = dao.getID("goods");
		Object name = map.get("name");
		Object category = map.get("category");
		Object unit = map.get("unit");
		Object spec = map.get("spec");
		Object purchase_price = map.get("purchase_price");
		Object sale_price = map.get("sale_price");
		Object[] params = new Object[] { id, name, category, unit, spec, purchase_price, sale_price };
		dao.update(sql, params);
	}

	@Transaction
	public void del(String ids)
	{
		String[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from purchase where userid in (");
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
		StringBuffer sql2 = new StringBuffer("delete from purchase where id in (");
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
		String sql = "update purchase set username=?,depart=?,memo=?,phone=?,email=?,gender=?,state=? where id=?";
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
