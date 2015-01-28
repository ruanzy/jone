package action;

import java.io.File;
import java.util.Map;
import org.rzy.util.IOUtil;
import org.rzy.util.WebUtil;
import org.rzy.web.View;
import org.rzy.web.view.Json;
import org.rzy.web.view.Text;

public class Log
{
	public View list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("PmsService.findlog", map));
	}

	public View users()
	{
		return new Json(WebUtil.call("PmsService.alluser"));
	}

	public View view()
	{
		File f = new File(WebUtil.getWebRoot(), "logs/Jone.txt");
		String content = IOUtil.tail(f, 50L);
		return new Text("<pre>" + content + "</pre>");
	}
}
