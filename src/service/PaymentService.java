package service;

import java.util.List;
import java.util.Map;
import com.rz.common.Record;
import com.rz.dao.Dao;
import com.rz.dao.SQLMapper;
import com.rz.sql.Pager;
import com.rz.tx.Transaction;

public class PaymentService {
	private Dao dao = Dao.getInstance();
	
	public void payableadd(Map<String, String> map)
	{
		String sql = "insert into goods_category(id,name) values(?,?)";
		int id = dao.getID("goods_category");
		Object name = map.get("name");
		Object[] params = new Object[] { id, name };
		dao.update(sql, params);
	}

	@Transaction
	public void payabledel(String ids)
	{
		Object[] arr = ids.split(",");
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

	public void payablemod(Map<String, Object> map)
	{
		String sql = "update goods_category set name=? where id=?";
		Object name = map.get("name");
		Object id = map.get("id");
		Object[] params = new Object[] { name, id };
		dao.update(sql, params);
	}
	
	public Pager pager(Map<String, String> map)
	{
		String sqlid2 = "payment.selectAll";
		Pager pager = SQLMapper.pager(sqlid2, map);
		return pager;
	}
	
	public List<Record> detailllist(String purchasebill)
	{
		String sql = "select * from payment_detail where no=?";
		Object[] params = new Object[] { purchasebill };
		return dao.find(sql, params);
	}
	
	public void recordadd(Map<String, String> map){
		String sql = "insert into payment_detail(no,paymoney,paytime,payuser,creator) values(?,?,?,?,?)";
		Object no = map.get("no");
		Object paymoney = Double.valueOf(map.get("paymoney").toString());
		Object paytime = map.get("paytime");
		Object payuser = map.get("payuser");
		Object creator = map.get("creator");
		Object[] params = new Object[] { no, paymoney, paytime, payuser, creator };
		dao.update(sql, params);
		String sql2 = "update payment set payment_made=payment_made+? where no=?";
		Object[] params2 = new Object[] { paymoney, no };
		dao.update(sql2, params2);
	}

	public void receivableadd(Map<String, String> map)
	{
		String sql = "insert into goods_category(id,name) values(?,?)";
		int id = dao.getID("goods_category");
		Object name = map.get("name");
		Object[] params = new Object[] { id, name };
		dao.update(sql, params);
	}

	@Transaction
	public void receivabledel(String ids)
	{
		Object[] arr = ids.split(",");
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

	public void receivablemod(Map<String, Object> map)
	{
		String sql = "update goods_category set name=? where id=?";
		Object name = map.get("name");
		Object id = map.get("id");
		Object[] params = new Object[] { name, id };
		dao.update(sql, params);
	}
	
	public Pager receivablepager(Map<String, String> map)
	{
		String sqlid2 = "receivable.selectAll";
		Pager pager = SQLMapper.pager(sqlid2, map);
		return pager;
	}
}
