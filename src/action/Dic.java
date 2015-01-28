package action;

import java.util.List;
import java.util.Map;
import org.rzy.util.JSONUtil;
import org.rzy.util.WebUtil;
import org.rzy.web.View;
import org.rzy.web.view.Json;
import org.rzy.web.view.Msg;

public class Dic
{
	public View list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("PmsService.finddic", map));
	}

	public View add()
	{
		Map<String, String> map = WebUtil.getParameters();
		WebUtil.call("PmsService.adddic", map);
		return new Msg("add success");
	}

	public View save()
	{
		String data = WebUtil.getParameter("data");
		List<Map<String, Object>> list = JSONUtil.toList(data);
		for (Map<String, Object> map : list)
		{
			WebUtil.call("PmsService.adddic", map);
		}
		return new Msg("add success");
	}

	public String del()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("PmsService.deldic", ids);
		return null;
	}
}
