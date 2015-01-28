package action;

import java.util.HashMap;
import java.util.Map;
import com.rz.util.ServerInfo;
import com.rz.util.WebUtil;
import com.rz.web.View;
import com.rz.web.view.Ftl;

public class Layout
{
	public View welcome()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", WebUtil.getParameter("id"));
		map = ServerInfo.base();
		return new Ftl("welcome.ftl", map);
	}

	public View header()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", WebUtil.getUser());
		return new Ftl("header.ftl", map);
	}

	public View center()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		return new Ftl("center.ftl", map);
	}
}
