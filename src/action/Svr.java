package action;

import java.util.Map;
import com.rz.util.ServerInfo;
import com.rz.web.Json;
import com.rz.web.View;
import com.rz.web.view.Ftl;

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
