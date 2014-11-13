package action;

import java.util.HashMap;
import java.util.Map;
import org.rzy.util.ServerInfo;
import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Ftl;

public class Layout
{
	public Result welcome()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", WebUtil.getParameter("id"));
		map = ServerInfo.base();
		return new Ftl("welcome.ftl", map);
	}
	
	public Result header()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", WebUtil.getUser());
		return new Ftl("header.ftl", map);
	}
	
	public Result center()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		return new Ftl("center.ftl", map);
	}
}
