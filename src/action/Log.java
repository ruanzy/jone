package action;

import java.io.File;
import java.util.Map;
import com.rz.util.IOUtil;
import com.rz.util.WebUtil;
import com.rz.web.Json;
import com.rz.web.View;
import com.rz.web.view.Text;

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
