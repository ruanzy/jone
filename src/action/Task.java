package action;

import java.util.Map;
import service.TaskService;
import com.rz.common.R;
import com.rz.web.Json;
import com.rz.web.View;
import com.rz.web.WebUtil;

public class Task
{
	public View list()
	{
		Map<String, String> params = WebUtil.getParameters();
		int page = Integer.valueOf(params.get("page"));
		int pagesize = Integer.valueOf(params.get("pagesize"));
		R ret = TaskService.list(params, page, pagesize);
		return new Json(ret);
	}
	
	public View add()
	{
		R params = WebUtil.getParams();
		TaskService.add(params);
		return new Json("");
	}
}
