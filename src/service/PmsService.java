package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.rz.annotation.Transaction;
import com.rz.dao.Dao;
import com.rz.dao.Pager;
import com.rz.dao.SQLMapper;
import com.rz.util.CryptUtil;
import com.rz.util.StringUtils;
import com.rz.util.TimeUtil;

public class PmsService
{
	private Dao dao = Dao.getInstance();

	public Pager finduser(Map<String, Object> map)
	{
		String sqlid1 = "user.count";
		String sqlid2 = "user.selectAll";
		Pager pager = SQLMapper.pager(sqlid1, sqlid2, map);
		return pager;
	}

	public void adduser(Map<String, Object> map)
	{
		String sql = "insert into topic(id,title,memo,content,creator,createtime,updatetime,toptime) values(?,?,?,?,?,?,?,?)";
		int id = dao.getID("topic");
		Object title = map.get("title");
		Object memo = map.get("memo");
		Object content = map.get("content");
		Object creator = map.get("creator");
		String time = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { id, title, memo, content, creator, time, time, time };
		dao.update(sql, params);
	}

	@Transaction
	public void deluser(String ids)
	{
		String[] arr = ids.split(",");
		StringBuffer sql1 = new StringBuffer("delete from userrole where userid in (");
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
		StringBuffer sql2 = new StringBuffer("delete from users where id in (");
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

	public void moduser(Map<String, Object> map)
	{
		String sql = "update users set username=?,memo=?,phone=?,email=?,gender=? where id=?";
		Object username = map.get("username");
		Object memo = map.get("memo");
		Object phone = map.get("phone");
		Object email = map.get("email");
		Object gender = map.get("gender");
		Object id = map.get("id");
		Object[] params = new Object[] { username, memo, phone, email, gender, id };
		dao.update(sql, params);
	}

	public void activeuser(String ids)
	{
		StringBuffer sql = new StringBuffer("update users set state=1 where id in(");
		String[] arr = ids.split(",");
		for (int k = 0, len = arr.length; k < len; k++)
		{
			sql.append("?");
			if (k != len - 1)
			{
				sql.append(",");
			}
		}
		sql.append(")");
		dao.update(sql.toString(), arr);
	}

	public void canceluser(String ids)
	{
		StringBuffer sql = new StringBuffer("update users set state=0 where id in(");
		String[] arr = ids.split(",");
		for (int k = 0, len = arr.length; k < len; k++)
		{
			sql.append("?");
			if (k != len - 1)
			{
				sql.append(",");
			}
		}
		sql.append(")");
		dao.update(sql.toString(), arr);
	}

	public Map<String, Object> getuser(String id)
	{
		Map<String, Object> map = null;
		String sql = "select * from topic where id=?";
		List<Map<String, Object>> list = dao.find(sql, new Object[] { id });
		if (list.size() == 1)
		{
			map = list.get(0);
		}
		return map;
	}

	public List<Map<String, Object>> getContents(String id)
	{
		String sql = "select * from reply where tid=? order by createtime desc";
		return dao.find(sql, new Object[] { id });
	}

	public Map<String, Object> login(String username, String password)
	{
		Map<String, Object> result = null;
		String sql = "select * from users where username=? and pwd=?";
		password = CryptUtil.encrypt(username + password);
		List<Map<String, Object>> list = dao.find(sql, new Object[] { username, password });
		if (list.size() == 1)
		{
			result = list.get(0);
		}
		return result;
	}

	public void addContent(Map<String, Object> map)
	{
		String sql = "insert into reply(id,tid,content,creator,createtime) values(?,?,?,?,?)";
		int id = dao.getID("reply");
		Object tid = map.get("tid");
		Object content = map.get("content");
		Object creator = map.get("creator");
		String createtime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { id, tid, content, creator, createtime };
		dao.update(sql, params);
	}

	public void reg(Map<String, Object> map)
	{
		String sql = "insert into users(id,username,pwd,depart,birth,gender,regtime,email,phone,memo,state) values(?,?,?,?,?,?,?,?,?,?,?)";
		int id = dao.getID("users");
		String username = (String) map.get("username");
		String pwd = "111111";
		pwd = CryptUtil.encrypt(username + pwd);
		String regtime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		String email = (String) map.get("email");
		String phone = (String) map.get("phone");
		String memo = (String) map.get("memo");
		String state = (String) map.get("state");
		String birth = (String) map.get("birth");
		String gender = (String) map.get("gender");
		String depart = (String) map.get("depart");
		Object[] params = new Object[] { id, username, pwd, depart, birth, gender, regtime, email, phone, memo, state };
		dao.update(sql, params);
	}

	public void top(String id)
	{
		String sql = "update topic set toptime=? where id=?";
		String toptime = TimeUtil.now("yyyy-MM-dd HH:mm:ss");
		Object[] params = new Object[] { toptime, id };
		dao.update(sql, params);
	}

	public List<Map<String, Object>> getTopics()
	{
		String sql = "select * from topic";
		return dao.find(sql);
	}

	public void delContent(String id)
	{
		String sql = "delete from reply where id=?";
		Object[] params = new Object[] { id };
		dao.update(sql, params);
	}

	public String userexist(String username)
	{
		String count = "0";
		String sql = "select count(*) from users where username=?";
		Object[] params = new Object[] { username };
		Object scalar = dao.scalar(sql, params);
		if (scalar != null)
		{
			count = scalar.toString();
		}
		return count;
	}

	public List<Map<String, Object>> getCatalogs()
	{
		String sql = "select * from catalog order by value";
		return dao.find(sql);
	}

	public Pager findlog(Map<String, Object> map)
	{
		String sqlid1 = "log.count";
		String sqlid2 = "log.selectAll";
		Pager pager = SQLMapper.pager(sqlid1, sqlid2, map);
		return pager;
	}

	public void addcatalog(Map<String, Object> map)
	{
		String sql = "insert into catalog(text,value) values(?,?)";
		Object text = map.get("text");
		Object value = map.get("value");
		Object[] params = new Object[] { text, value };
		dao.update(sql, params);
	}

	public void addBatchcatalog(List<Map<String, Object>> list)
	{
		String sql = "insert into catalog(text,value) values(?,?)";
		List<Object[]> params = new ArrayList<Object[]>();
		for (Map<String, Object> map2 : list)
		{

			Object text = map2.get("text");
			Object value = map2.get("value");
			params.add(new Object[] { text, value });
		}
		dao.beginBatch(sql);
		for (Map<String, Object> map2 : list)
		{
			Object text = map2.get("text");
			Object value = map2.get("value");
			dao.addBatch(new Object[] { text, value });
		}
		dao.excuteBatch();
		dao.endBatch();
	}

	public void addres(Map<String, Object> map)
	{
		String sql = "insert into resources(id,name,url,icon,type,pid,path,method,flag) values(?,?,?,?,?,?,?,?,?)";
		int id = dao.getID("resources");
		Object type = map.get("type");
		Object url = map.get("url");
		Object name = map.get("name");
		Object method = map.get("method");
		String pid = String.valueOf(map.get("pid"));
		Object icon = map.get("icon");
		String path = String.valueOf(id);
		if (!"1".equals(type))
		{
			path = getParentPath(pid) + "/" + id;
		}
		Object[] params = new Object[] { id, name, url, icon, type, pid, path, method, 1 };
		dao.update(sql, params);
	}

	private String getParentPath(String pid)
	{
		String sql = "select path from resources where id = ?";
		Object[] params = new Object[] { pid };
		String path = (String) dao.scalar(sql, params);
		return path;
	}

	public List<Map<String, Object>> getRes()
	{
		String sql = "select r.*,'true' as open from resources r where r.id>=100";
		return dao.find(sql);
	}

	public List<Map<String, Object>> menubymoudle(String pid)
	{
		String sql = "select * from resources where pid=?";
		Object[] params = new Object[] { pid };
		return dao.find(sql, params);
	}

	public Pager finddic(Map<String, Object> map)
	{
		String sqlid1 = "dic.count";
		String sqlid2 = "dic.selectAll";
		Pager pager = SQLMapper.pager(sqlid1, sqlid2, map);
		return pager;
	}

	public void adddic(Map<String, Object> map)
	{
		String sql = "insert into dic(id,name,val,type,memo) values(?,?,?,?,?)";
		int id = dao.getID("dic");
		Object type = map.get("type");
		Object val = map.get("val");
		Object name = map.get("name");
		Object memo = map.get("memo");
		Object[] params = new Object[] { id, name, val, type, memo };
		dao.update(sql, params);
	}

	public void deldic(String ids)
	{
		StringBuffer sql = new StringBuffer("delete from dic where id in(");
		String[] arr = ids.split(",");
		for (int k = 0, len = arr.length; k < len; k++)
		{
			sql.append("?");
			if (k != len - 1)
			{
				sql.append(",");
			}
		}
		sql.append(")");
		dao.update(sql.toString(), arr);
	}

	public void moddic(Map<String, Object> map)
	{
		String sql = "update dic set name=?,val=?,type=? where id=?";
		Object id = map.get("id");
		Object type = map.get("type");
		Object val = map.get("val");
		Object name = map.get("name");
		Object[] params = new Object[] { name, val, type, id };
		dao.update(sql, params);
	}

	public Object menu()
	{
		String sql = "select * from resources where type in(1,2)";
		return dao.find(sql);
	}

	public Pager findrole(Map<String, Object> map)
	{
		String sqlid1 = "role.count";
		String sqlid2 = "role.selectAll";
		Pager pager = SQLMapper.pager(sqlid1, sqlid2, map);
		return pager;
	}

	public void addrole(Map<String, Object> map)
	{
		String sql = "insert into role(id,name) values(?,?)";
		int id = dao.getID("role");
		Object name = map.get("name");
		Object[] params = new Object[] { id, name };
		dao.update(sql, params);
	}

	@Transaction
	public void setres(String role, String res)
	{
		String sql1 = "delete from roleres where roleid=?";
		dao.update(sql1, new Object[] { role });
		if (StringUtils.isNotBlank(res))
		{
			String sql2 = "insert into roleres(roleid,resid) values(?,?)";
			String[] arr = res.split(",");
			for (String r : arr)
			{
				Object[] params = new Object[] { role, r };
				dao.update(sql2, params);
			}
		}
	}

	public List<Map<String, Object>> allrole()
	{
		String sql = "select * from role";
		return dao.find(sql);
	}
	
	public List<Map<String, Object>> userrole(String user)
	{
		String sql = "select r.id, r.name, EXISTS(select * from userrole where userrole.userid = ? and userrole.roleid = r.id) as checked from role r";
		Object[] params = new Object[] { user };
		return dao.find(sql, params);
	}

	@Transaction
	public void setrole(String user, String roles)
	{
		String sql1 = "delete from userrole where userid=?";
		dao.update(sql1, new Object[] { user });
		if (StringUtils.isNotBlank(roles))
		{
			String sql2 = "insert into userrole(userid,roleid) values(?,?)";
			String[] arr = roles.split(",");
			dao.beginBatch(sql2);
			for (String r : arr)
			{
				dao.addBatch(new Object[] { user, r });
			}
			dao.excuteBatch();
			dao.endBatch();
		}
	}

	public List<String> assignedroles(String user)
	{
		List<String> ret = new ArrayList<String>();
		String sql = "select roleid from userrole where userid=?";
		List<Map<String, Object>> list = dao.find(sql, new Object[] { user });
		for (Map<String, Object> map : list)
		{
			ret.add(String.valueOf(map.get("roleid")));
		}
		return ret;
	}

	public Object roleres(String role)
	{
		List<String> ret = new ArrayList<String>();
		String sql = "select resid from roleres where roleid=?";
		List<Map<String, Object>> list = dao.find(sql, new Object[] { role });
		for (Map<String, Object> map : list)
		{
			ret.add(String.valueOf(map.get("resid")));
		}
		return ret;
	}

	@Transaction
	public void delres(String id)
	{
		String sql1 = "delete from roleres where resid in(select id from resources where path like '%" + id + "%')";
		String sql = "delete from resources where path like '%" + id + "%'";
		dao.update(sql1);
		dao.update(sql);
	}

	@Transaction
	public void delroles(String id)
	{
		String sql1 = "delete from roleres where roleid=" + id;
		String sql2 = "delete from userrole where roleid=" + id;
		String sql3 = "delete from role where id=" + id;
		dao.update(sql1);
		dao.update(sql2);
		dao.update(sql3);
	}

	public List<Map<String, Object>> userres(String user)
	{
		String sql2 = "select * from resources where id in(select distinct resid from roleres where roleid in (select roleid from userrole where userid=?))";
		Object[] params = new Object[] { user };
		return dao.find(sql2, params);
	}

	public List<Map<String, Object>> dics()
	{

		String sql = "select * from dic";
		return dao.find(sql);
	}

	public List<Map<String, Object>> res()
	{
		String sql = "select * from resources";
		return dao.find(sql);
	}

	public List<Map<String, Object>> alluser()
	{
		String sql = "select u.id as value, u.username as text from users u";
		return dao.find(sql);
	}

	public List<Map<String, Object>> test(Map<String, Object> p, int page, int pagesize)
	{
		String sql = "select * from users";
		return dao.pager(sql, page, pagesize);
	}
	
	public static void main(String[] args)
	{
		List<Map<String, Object>> list = new PmsService().userrole("用户测试");
		for (Map<String, Object> map : list)
		{
			System.out.println(map.get("name")+ "==" + map.get("checked"));
		}
	}
}
