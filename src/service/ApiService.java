package service;

import java.util.List;
import java.util.Map;
import com.rz.dao.Dao;

public class ApiService
{
	private Dao dao = Dao.getInstance();
	
	public List<Map<String,Object>> find(String apiid)
	{
		String sql = "select * from apiparam where apiid=?";
		Object[] params = new Object[] { apiid };
		return dao.find(sql, params);
	}
	
	public void add(Map<String, Object> map)
	{
		String sql = "insert into apiparam(apiid,pname,required,memo) values(?,?,?,?)";
		Object apiid = map.get("apiid");
		Object pname = map.get("pname");
		Object required = map.get("required");
		Object memo = map.get("memo");
		Object[] params = new Object[] { apiid, pname, required, memo };
		dao.update(sql, params);
	}
	
	public void del(String apiid, String pname)
	{
		String sql = "delete from apiparam where apiid=? and pname=?";
		Object[] params = new Object[] { apiid, pname };
		dao.update(sql, params);
	}
	
	public void mod(Map<String, Object> map)
	{
		String sql = "update apiparam set pname=?,required=?,memo=? where apiid=?";
		Object apiid = map.get("apiid");
		Object pname = map.get("pname");
		Object required = map.get("required");
		Object memo = map.get("memo");
		Object[] params = new Object[] { pname, required, memo, apiid };
		dao.update(sql, params);
	}
}
