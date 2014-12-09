package action;

import java.io.File;
import java.util.Map;
import org.rzy.util.IOUtil;
import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Json;
import org.rzy.web.result.Text;

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
	
	public Result view()
	{
		File f = new File(WebUtil.getWebRoot(), "logs/Jone.txt");
		String content = IOUtil.tail(f, 50L);
		return new Text("<pre>" + content + "</pre>");
	}
}
