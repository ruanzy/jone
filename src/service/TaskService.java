package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.rz.common.R;
import com.rz.dao.DB;
import com.rz.dao.DBs;
import com.rz.dao.sql.SQLExecutor;

public class TaskService {
	static DB db = DBs.getDB("hsqldb");

	public static R list(Map<String, String> params, int page, int pagesize) {
		R ret = new R();
		SQLExecutor executor = new SQLExecutor(db);
		Object count = executor.scalar("customer.count", null);
		int total = Integer.valueOf(count.toString());
		List<R> data = new ArrayList<R>();
		if (total > 0) {
			data = executor.pager("customer.list", null, page, pagesize);
		}
		ret.put("total", total);
		ret.put("data", data);
		return ret;
	}
	
	public static void add(R r) {
		String name = r.getString("name");
		String time = r.getString("time");
		String amount = r.getString("amount");
		String tax = r.getString("tax");
		String memo = r.getString("memo");
		String sql = "insert into customer values(?,?,?,?,?)";
		Object[] params = new Object[]{name, time, amount, tax, memo};
		db.update(sql, params);
	}
}
