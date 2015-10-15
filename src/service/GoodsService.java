package service;

import java.util.List;
import java.util.Map;
import com.rz.common.Record;
import com.rz.dao.DB;
import com.rz.dao.SQLMapper;
import com.rz.sql.Pager;
import com.rz.tx.Transaction;

public class GoodsService
{
	private DB dao = DB.getInstance();
	
	public List<Record> category()
	{
		String sql = "select * from goods_category";
		return dao.find(sql);
	}
	
	public List<Record> unitlist()
	{
		String sql = "select * from goods_unit";
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

	public void categorymod(Map<String, Object> map)
	{
		String sql = "update goods_category set name=? where id=?";
		Object name = map.get("name");
		Object id = map.get("id");
		Object[] params = new Object[] { name, id };
		dao.update(sql, params);
	}

	public void unitadd(Map<String, String> map)
	{
		String sql = "insert into goods_unit(id,name) values(?,?)";
		int id = dao.getID("goodsunit");
		Object name = map.get("name");
		Object[] params = new Object[] { id, name };
		dao.update(sql, params);
	}

	@Transaction
	public void unitdel(String ids)
	{
		Object[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from goods_unit where id in (");
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
		StringBuffer sql2 = new StringBuffer("delete from goods_unit where id in (");
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

	public void unitmod(Map<String, Object> map)
	{
		String sql = "update goods_unit set name=? where id=?";
		Object name = map.get("name");
		Object id = map.get("id");
		Object[] params = new Object[] { name, id };
		dao.update(sql, params);
	}

	public List<Record> list(Map<String, String> map)
	{
		String sql = "select * from goods";
		return dao.find(sql);
	}
	
	public Pager pager(Map<String, String> map)
	{
		String sqlid2 = "goods.selectAll";
		Pager pager = SQLMapper.pager(sqlid2, map);
		return pager;
	}

	public void add(Map<String, String> map)
	{
		String sql = "insert into goods(id,name,category,unit,spec) values(?,?,?,?,?)";
		int id = dao.getID("goods");
		Object name = map.get("name");
		Object category = map.get("category");
		Object unit = map.get("unit");
		Object spec = map.get("spec");
		Object[] params = new Object[] { id, name, category, unit, spec };
		dao.update(sql, params);
	}

	public void del(String ids)
	{
		Object[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from goods where id in (");
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
		String sql = "update goods set name=?,category=?,unit=?,spec=? where id=?";
		Object name = map.get("name");
		Object category = map.get("category");
		Object unit = map.get("unit");
		Object spec = map.get("spec");
		Object id = map.get("id");
		Object[] params = new Object[] { name, category, unit, spec, id };
		dao.update(sql, params);
	}
}
