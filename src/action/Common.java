package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.rzy.util.ServerInfo;
import org.rzy.web.ApplicationUtil;
import org.rzy.web.RequestUtil;
import org.rzy.web.Result;
import org.rzy.web.SessionUtil;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Ftl;
import org.rzy.web.result.Json;
import org.rzy.web.result.Page;

public class Common
{
	@SuppressWarnings("unchecked")
	public Result menu()
	{
		String user = SessionUtil.getUser();
		Object o = null;
		List<Map<String, Object>> res = null;
		List<Map<String, Object>> menus = null;
		if ("admin".equals(user))
		{
			o = ApplicationUtil.attr("allres");
		}
		else
		{
			o = SessionUtil.attr("res");
		}

		if (o != null)
		{
			res = (List<Map<String, Object>>) o;
			menus = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : res)
			{
				String type = String.valueOf(map.get("type"));
				if (!"3".equals(type))
				{
					menus.add(map);
				}
			}
		}
		return new Json(menus);
	}

	@SuppressWarnings("unchecked")
	public Result op()
	{
		String user = SessionUtil.getUser();
		Object o = null;
		List<Map<String, Object>> res = null;
		List<Map<String, Object>> ops = null;
		if ("admin".equals(user))
		{
			o = ApplicationUtil.attr("allres");
		}
		else
		{
			o = SessionUtil.attr("res");
		}

		if (o != null)
		{
			res = (List<Map<String, Object>>) o;
			ops = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> map : res)
			{
				String type = String.valueOf(map.get("type"));
				if ("3".equals(type))
				{
					ops.add(map);
				}
			}
		}
		return new Json(ops);
	}

	public Result dic()
	{
		Object o = ApplicationUtil.attr("dic");
		return new Json(o);
	}

	public Result res()
	{
		Object o = ApplicationUtil.attr("allres");
		return new Json(o);
	}

	public Result lineusers()
	{
		Integer numSessions = (Integer) ApplicationUtil.attr("numSessions");
		return new Json(numSessions);
	}

	@SuppressWarnings("unchecked")
	public static void loadResandDic()
	{
		Object allres = ApplicationUtil.attr("allres");
		if (allres == null)
		{
			ApplicationUtil.attr("allres", WebUtil.call("PmsService.res"));
		}
		Object o = ApplicationUtil.attr("dic");
		if (o == null)
		{
			Map<String, Map<String, Object>> dic = new HashMap<String, Map<String, Object>>();
			List<Map<String, Object>> all = (List<Map<String, Object>>) WebUtil.call("PmsService.dics");
			for (Map<String, Object> m : all)
			{
				String key = String.valueOf(m.get("type"));
				String name = String.valueOf(m.get("name"));
				String val = String.valueOf(m.get("val"));
				if (!dic.containsKey(key))
				{
					dic.put(key, new HashMap<String, Object>());
				}
				dic.get(key).put(val, name);
			}
			ApplicationUtil.attr("dic", dic);
		}
	}

	public Result logout()
	{
		SessionUtil.clear();
		return new Page("login.html", true);
	}

	public Result welcome()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", RequestUtil.getParameter("id"));
		map = ServerInfo.base();
		return new Ftl("welcome.ftl", map);
	}
	
	public Result header()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		String user = SessionUtil.getUser();
		data.put("user", user);
		return new Ftl("header.ftl", data);
	}
	
	public Result sidebar()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		String user = SessionUtil.getUser();
		Object o = null;
		if ("admin".equals(user))
		{
			o = ApplicationUtil.attr("allres");
		}
		else
		{
			o = SessionUtil.attr("res");
		}
		data.put("all", o);
		return new Ftl("sidebar.ftl", data);
	}
	
	public Result center()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		return new Ftl("center.ftl", map);
	}

	public void upload()
	{
		// String path = "d:/upload/";
		// Uploader uploader = Uploader.prepare();
		// Map<String, String> parameters = uploader.getParameters();
		// List<FileItem> items = uploader.getItems();
		// String flag = parameters.get("flag");
		// String id = parameters.get("id");
	}

	public Result base()
	{
		Map<String, Object> map = ServerInfo.base();
		return new Json(map);
	}
	
	public Result userinfo()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		String user = SessionUtil.getUser();
		data.put("user", user);
		return new Ftl("userinfo.ftl", data);
	}
}
