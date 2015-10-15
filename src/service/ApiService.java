package service;

import java.util.List;
import java.util.Map;
import com.rz.common.Record;
import com.rz.dao.DB;

public class ApiService
{
	private DB dao = DB.getInstance();
	
	public List<Record> find(String apiid)
	{
		String sql = "select * from apiparam where apiid=?";
		Object[] params = new Object[] { apiid };
		return dao.find(sql, params);
	}
	
	public void add(Map<String, Object> map)
	{
		String sql = "insert into apiparam(id,pname,required,memo,apiid) values(?,?,?,?,?)";
		Object id = dao.getID("apiparam");
		Object apiid = map.get("apiid");
		Object pname = map.get("pname");
		Object required = map.get("required");
		Object memo = map.get("memo");
		Object[] params = new Object[] { id, pname, required, memo, apiid };
		dao.update(sql, params);
	}
	
	public void del(String id)
	{
		String sql = "delete from apiparam where id=?";
		Object[] params = new Object[] { id };
		dao.update(sql, params);
	}
	
	public void mod(Map<String, Object> map)
	{
		String sql = "update apiparam set pname=?,required=?,memo=? where id=?";
		Object pname = map.get("pname");
		Object required = map.get("required");
		Object memo = map.get("memo");
		Object id = map.get("id");
		Object[] params = new Object[] { pname, required, memo, id };
		dao.update(sql, params);
	}
}
