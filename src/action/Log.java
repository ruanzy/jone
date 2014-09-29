package action;

import java.util.Map;

import org.rzy.web.Context;
import org.rzy.web.Result;
import org.rzy.web.result.Json;
import org.rzy.web.util.XUtil;

public class Log
{
	public Result list()
	{
		Map<String, String> map = Context.getParameters();
		return new Json(XUtil.call("PmsService.findlog", map));
	}

	public Result users()
	{
		return new Json(XUtil.call("PmsService.alluser"));
	}
}
