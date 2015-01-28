package action;

import java.util.Map;
import org.rzy.util.ServerInfo;
import org.rzy.web.View;
import org.rzy.web.view.Ftl;
import org.rzy.web.view.Json;

public class Svr
{
	public View base()
	{
		Map<String, Object> map = ServerInfo.base();
		return new Ftl("welcome.ftl", map);
	}
	
	public View mem()
	{
		Map<String, Object> map = ServerInfo.mem();
		return new Json(map);
	}
}
