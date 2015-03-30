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
	
	public List<Map<String, Object>> detaillist(String purchasebill)
	{
		String sql = "select * from pbill_detail left join goods on goods.id=pbill_detail.goods where pbill_detail.purchase_bill=?";
		Object[] params = new Object[] { purchasebill };
		return dao.find(sql, params);
	}
	
	public void income(String no)
	{
		String sql1 = "insert into payment(no,supplier,payment,payment_made, payment_due) select no,supplier,money,0.00,money from purchase_bill where no=?";
		String sql2 = "update purchase_bill set state=1 where no=?";
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
		String sql2 = "insert into pbill_detail(purchase_bill,goods,purchase_num,purchase_price,purchase_money) values(?,?,?,?,?)";
		dao.beginBatch(sql2);
		for (Map<String, Object> map2 : detail)
		{
			Object goods = map2.get("goods");
			Object purchase_num = map2.get("purchase_num");
			Object purchase_price = map2.get("purchase_price");
			double purchase_money = Double.valueOf(map2.get("purchase_money").toString());
			money += purchase_money;
			Object[] params2 = new Object[] { no, goods, purchase_num, purchase_price, purchase_money };
			dao.addBatch(params2);
		}
		dao.excuteBatch();
		dao.endBatch();
		String sql = "insert into purchase_bill(id,no,supplier,warehouse,creator,createtime,state,money) values(?,?,?,?,?,?,?,?)";
		int id = dao.getID("purchase_bill");
		Object supplier = map.get("supplier");
		Object warehouse = map.get("warehouse");
		Object creator = map.get("creator");
		Object createtime = map.get("createtime");
		Object[] params = new Object[] { id, no, supplier, warehouse, creator, createtime, 0, money };
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
