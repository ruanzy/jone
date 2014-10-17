package action;

import java.util.List;
import java.util.Map;
import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Json;

public class Dic
{
	public Result list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("PmsService.finddic", map));
	}

	public Result add()
	{
		Map<String, String> map = WebUtil.getParameters();
		WebUtil.call("PmsService.adddic", map);
		return new Json(true, "add success");
	}

	public Result save()
	{
		String data = WebUtil.getParameter("data");
		List<Map<String, Object>> list = WebUtil.toList(data);
		for (Map<String, Object> map : list)
		{
			WebUtil.call("PmsService.adddic", map);
		}
		return new Json(true, "add success");
	}

	public String del()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("PmsService.deldic", ids);
		return null;
	}
}
