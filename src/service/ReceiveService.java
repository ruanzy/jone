package service;

import java.util.List;
import java.util.Map;
import com.rz.common.Record;
import com.rz.dao.DB;
import com.rz.dao.SQLMapper;
import com.rz.sql.Pager;
import com.rz.tx.Transaction;

public class ReceiveService {
	private DB dao = DB.getInstance();
	
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
		String sqlid2 = "receive.selectAll";
		Pager pager = SQLMapper.pager(sqlid2, map);
		return pager;
	}
	
	public List<Record> detailllist(String no)
	{
		String sql = "select * from receive_detail where no=?";
		Object[] params = new Object[] { no };
		return dao.find(sql, params);
	}
	
	public void recordadd(Map<String, String> map){
		String sql = "insert into receive_detail(no,receivemoney,receivetime,receiveuser,creator) values(?,?,?,?,?)";
		Object no = map.get("no");
		Object receivemoney = Double.valueOf(map.get("receivemoney").toString());
		Object receivetime = map.get("receivetime");
		Object receiveuser = map.get("receiveuser");
		Object creator = map.get("creator");
		Object[] params = new Object[] { no, receivemoney, receivetime, receiveuser, creator };
		dao.update(sql, params);
		String sql2 = "update receive set receive_made=receive_made+? where no=?";
		Object[] params2 = new Object[] { receivemoney, no };
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
