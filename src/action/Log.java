package action;

import java.util.Map;
import org.rzy.mvc.XUtil;

public class Log
{
	public String list()
	{
		Map<String, Object> map = XUtil.getParameterMap();
		XUtil.calljson("PmsService.findlog", map);
		return null;
	}

	public String users()
	{
		XUtil.calljson("PmsService.alluser");
		return null;
	}
}
