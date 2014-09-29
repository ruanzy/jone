package action;

import java.util.List;
import java.util.Map;

import org.rzy.web.Context;
import org.rzy.web.Result;
import org.rzy.web.result.Json;
import org.rzy.web.util.XUtil;

public class Dic
{
	public Result list()
	{
		Map<String, String> map = Context.getParameters();
		return new Json(XUtil.call("PmsService.finddic", map));
	}

	public String add()
	{
		Map<String, String> map = Context.getParameters();
		XUtil.call("PmsService.adddic", map);
		XUtil.ok();
		return null;
	}

	public String save()
	{
		String data = Context.getParameter("data");
		List<Map<String, Object>> list = XUtil.toList(data);
		for (Map<String, Object> map : list)
		{
			XUtil.call("PmsService.adddic", map);
		}
		XUtil.ok();
		return null;
	}

	public String del()
	{
		String ids = Context.getParameter("ids");
		XUtil.call("PmsService.deldic", ids);
		return null;
	}
}
