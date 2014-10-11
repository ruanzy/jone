package action;

import java.util.Map;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Json;

public class Log
{
	public Result list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("PmsService.findlog", map));
	}

	public Result users()
	{
		return new Json(WebUtil.call("PmsService.alluser"));
	}
}
