package action;

import java.util.Map;
import org.rzy.web.util.XUtil;

public class Log
{
	public String list()
	{
		Map<String, String> map = XUtil.getParameters();
		XUtil.calljson("PmsService.findlog", map);
		return null;
	}

	public String users()
	{
		XUtil.calljson("PmsService.alluser");
		return null;
	}
}
