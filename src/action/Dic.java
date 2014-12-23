package action;

import java.util.List;
import java.util.Map;
import org.rzy.util.JSONUtil;
import org.rzy.web.RequestUtil;
import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Json;
import org.rzy.web.result.Msg;

public class Dic
{
	public Result list()
	{
		Map<String, String> map = RequestUtil.getParameters();
		return new Json(WebUtil.call("PmsService.finddic", map));
	}

	public Result add()
	{
		Map<String, String> map = RequestUtil.getParameters();
		WebUtil.call("PmsService.adddic", map);
		return new Msg("add success");
	}

	public Result save()
	{
		String data = RequestUtil.getParameter("data");
		List<Map<String, Object>> list = JSONUtil.toList(data);
		for (Map<String, Object> map : list)
		{
			WebUtil.call("PmsService.adddic", map);
		}
		return new Msg("add success");
	}

	public String del()
	{
		String ids = RequestUtil.getParameter("ids");
		WebUtil.call("PmsService.deldic", ids);
		return null;
	}
}
