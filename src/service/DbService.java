package service;

import java.util.List;
import com.rz.common.R;
import com.rz.data.db.DB;
import com.rz.data.db.DBs;

public class DbService
{
	static DB db = DBs.getDB("h2");

	public static List<R> exe(String sql, int page, int pagesize)
	{
		return db.pager(sql, page, pagesize);
	}

}
