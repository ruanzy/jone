package action;

import java.util.Map;
import org.rzy.util.ServerInfo;
import org.rzy.web.Result;
import org.rzy.web.result.Ftl;
import org.rzy.web.result.Json;

public class Svr
{
	public Result base()
	{
		Map<String, Object> map = ServerInfo.base();
		return new Ftl("welcome.ftl", map);
	}
	
	public Result mem()
	{
		Map<String, Object> map = ServerInfo.mem();
		return new Json(map);
	}
}
