package action;

import java.util.Map;
import com.rz.util.WebUtil;
import com.rz.web.Json;
import com.rz.web.View;
import com.rz.web.view.Msg;

public class Res
{
	public View list()
	{
		return new Json(WebUtil.call("PmsService.getRes"));
	}

	public View add()
	{
		Map<String, String> map = WebUtil.getParameters();
		WebUtil.call("PmsService.addres", map);
		return new Msg("增加成功");
	}

	public View del()
	{
		String id = WebUtil.getParameter("id");
		WebUtil.call("PmsService.delres", id);
		return new Msg("删除成功");
	}

	public View menubymoudle()
	{
		String pid = WebUtil.getParameter("pid");
		return new Json(WebUtil.call("PmsService.menubymoudle", pid));
	}
}
