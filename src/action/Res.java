package action;

import java.util.Map;
import org.rzy.web.RequestUtil;
import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Json;
import org.rzy.web.result.Msg;

public class Res
{
	public Result list()
	{
		return new Json(WebUtil.call("PmsService.getRes"));
	}

	public Result add()
	{
		Map<String, String> map = RequestUtil.getParameters();
		WebUtil.call("PmsService.addres", map);
		return new Msg("增加成功");
	}

	public Result del()
	{
		String id = RequestUtil.getParameter("id");
		WebUtil.call("PmsService.delres", id);
		return new Msg("删除成功");
	}

	public Result menubymoudle()
	{
		String pid = RequestUtil.getParameter("pid");
		return new Json(WebUtil.call("PmsService.menubymoudle", pid));
	}
}
