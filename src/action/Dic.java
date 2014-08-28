package action;

import java.util.List;
import java.util.Map;
import org.rzy.mvc.XUtil;

public class Dic
{
	public String list()
	{
		Map<String, Object> map = XUtil.getParameterMap();
		XUtil.calljson("PmsService.finddic", map);
		return null;
	}

	public String add()
	{
		Map<String, Object> map = XUtil.getParameterMap();
		XUtil.call("PmsService.adddic", map);
		XUtil.ok();
		return null;
	}

	public String save()
	{
		String data = XUtil.getParameter("data");
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
		String ids = XUtil.getParameter("ids");
		XUtil.call("PmsService.deldic", ids);
		return null;
	}
}
