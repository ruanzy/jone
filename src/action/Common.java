package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.rzy.util.ServerInfo;
import org.rzy.web.I18N;
import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Ftl;
import org.rzy.web.result.Json;
import org.rzy.web.result.Msg;
import org.rzy.web.result.Page;

public class Common
{
	@SuppressWarnings("unchecked")
	public Result menu()
	{
		String user = WebUtil.getUser();
		Object o = null;
		List<Map<String, Object>> res = null;
		List<Map<String, Object>> menus = null;
		if ("admin".equals(user))
		{
			o = WebUtil.attr("allres", "application");
		}
		else
		{
			o = WebUtil.attr("res", "session");
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
		String user = WebUtil.getUser();
		Object o = null;
		List<Map<String, Object>> res = null;
		List<Map<String, Object>> ops = null;
		if ("admin".equals(user))
		{
			o = WebUtil.attr("allres", "application");
		}
		else
		{
			o = WebUtil.attr("res", "session");
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
		Object o = WebUtil.attr("dic", "application");
		return new Json(o);
	}

	public Result res()
	{
		Object o = WebUtil.attr("allres", "application");
		return new Json(o);
	}

	public Result lineusers()
	{
		Integer numSessions = (Integer) WebUtil.attr("numSessions", "application");
		return new Json(numSessions);
	}

	public Result login()
	{
		String svc = WebUtil.getVC();
		String vc = WebUtil.getParameter("vc");
		String username = WebUtil.getParameter("username");
		String password = WebUtil.getParameter("password");
		if (!svc.equalsIgnoreCase(vc))
		{
			String msg = I18N.get("20000");
			return new Msg(false, msg);
		}
		if ("admin".equals(username))
		{
			if (!"162534".equals(password))
			{
				return new Msg(false, "用户名或密码错误！");
			}
			WebUtil.setUser("admin");
		}
		else
		{
			Object user = WebUtil.call("PmsService.login", username, password);
			if (user == null)
			{
				return new Msg(false, "用户名或密码错误！");
			}
			WebUtil.setUser(username);
			WebUtil.attr("res", WebUtil.call("PmsService.userres", username), "session");
		}
		loadResandDic();
		return new Msg(true, "login success");
	}

	@SuppressWarnings("unchecked")
	public static void loadResandDic()
	{
		Object allres = WebUtil.attr("allres", "application");
		if (allres == null)
		{
			WebUtil.attr("allres", WebUtil.call("PmsService.res"), "application");
		}
		Object o = WebUtil.attr("dic", "application");
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
			WebUtil.attr("dic", dic, "application");
		}
	}

	public Result logout()
	{
		WebUtil.clearSession();
		return new Page("login.html", true);
	}

	public Result welcome()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", WebUtil.getParameter("id"));
		map = ServerInfo.base();
		map.putAll(ServerInfo.mem());
		return new Ftl("welcome.ftl", map);
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
}
