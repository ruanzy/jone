package action;

import java.util.List;
import service.DbService;
import service.TaskService;
import com.rz.common.R;
import com.rz.web.WebUtil;

public class Db
{
	public List<R> exe()
	{
		R r = WebUtil.getParams();
		String sql = r.getString("sql");
		int page = r.getInt("page");
		int pagesize = r.getInt("pagesize");
		List<R> ret = DbService.exe(sql, page, pagesize);
		return ret;
	}
	
	public void add()
	{
		R params = WebUtil.getParams();
		TaskService.add(params);
	}
}
