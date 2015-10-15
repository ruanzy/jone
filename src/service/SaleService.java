package service;

import java.util.List;
import java.util.Map;
import com.rz.common.Record;
import com.rz.dao.DB;
import com.rz.dao.SQLMapper;
import com.rz.sql.Pager;
import com.rz.tx.Transaction;

public class SaleService
{
	private DB dao = DB.getInstance();

	public Pager outlist(Map<String, String> map)
	{
		String sqlid2 = "saleout.selectAll";
		Pager pager = SQLMapper.pager(sqlid2, map);
		return pager;
	}
	
	public Pager returnlist(Map<String, String> map)
	{
		String sqlid2 = "salereturn.selectAll";
		Pager pager = SQLMapper.pager(sqlid2, map);
		return pager;
	}
	
	public List<Record> detaillist(String purchasebill)
	{
		String sql = "select * from sale_detail left join goods on goods.id=sale_detail.goods where sale_detail.no=?";
		Object[] params = new Object[] { purchasebill };
		return dao.find(sql, params);
	}
	
	public void out(String no)
	{
		String sql1 = "insert into receive(no,customer,receive,receive_made, receive_due) select no,customer,money,0.00,money from saleout where no=?";
		String sql2 = "update saleout set state=1 where no=?";
		Object[] params = new Object[] { no };
		dao.update(sql1, params);
		dao.update(sql2, params);
	}

	@SuppressWarnings("unchecked")
	@Transaction
	public void add(Map<String, Object> map)
	{
		Object no = map.get("no");
		double money = 0.0;
		List<Map<String, Object>> detail = (List<Map<String, Object>>)(map.get("detail"));
		String sql2 = "insert into sale_detail(id,no,goods,sale_num,sale_price,sale_money) values(?,?,?,?,?,?)";
		dao.beginBatch(sql2);
		for (Map<String, Object> map2 : detail)
		{
			int id = dao.getID("sale_detail");
			Object goods = map2.get("goods");
			Object sale_num = map2.get("sale_num");
			Object sale_price = map2.get("sale_price");
			double sale_money = Double.valueOf(map2.get("sale_money").toString());
			money += sale_money;
			Object[] params2 = new Object[] { id, no, goods, sale_num, sale_price, sale_money };
			dao.addBatch(params2);
		}
		dao.excuteBatch();
		dao.endBatch();
		String sql = "insert into saleout(id,no,customer,warehouse,creator,createtime,state,money) values(?,?,?,?,?,?,?,?)";
		int id = dao.getID("saleout");
		Object customer = map.get("customer");
		Object warehouse = map.get("warehouse");
		Object creator = map.get("creator");
		Object createtime = map.get("createtime");
		Object[] params = new Object[] { id, no, customer, warehouse, creator, createtime, 0, money };
		dao.update(sql, params);
	}

	@Transaction
	public void del(String ids)
	{
		Object[] arr = ids.split(",");
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
