package action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import org.rzy.web.result.Ftl;
import org.rzy.web.result.Json;

public class Common
{
	@SuppressWarnings("unchecked")
	public Result menu()
	{
		Object o = WebUtil.attr("res", "session");
		List<Map<String, Object>> list = null;
		if (o != null)
		{
			Map<String, List<Map<String, Object>>> userres = (Map<String, List<Map<String, Object>>>) o;
			list = userres.get("menus");
		}
		return new Json(list);
	}

	@SuppressWarnings("unchecked")
	public Result op()
	{
		Object o = WebUtil.attr("res", "session");
		List<Map<String, Object>> list = null;
		if (o != null)
		{
			Map<String, List<Map<String, Object>>> userres = (Map<String, List<Map<String, Object>>>) o;
			list = userres.get("ops");
		}
		return new Json(list);
	}

	@SuppressWarnings("unchecked")
	public Result dic()
	{
		String type = WebUtil.getParameter("type");
		Map<String, Object> map = null;
		Object o = WebUtil.attr("dic", "application");
		if (o != null)
		{
			Map<String, Map<String, Object>> dics = (Map<String, Map<String, Object>>) o;
			map = dics.get(type);
		}
		return new Json(map);
	}

	public Result lineusers()
	{
		Integer numSessions = (Integer) WebUtil.attr("numSessions", "application");
		return new Json(numSessions);
	}

	public String login()
	{
		String svc = WebUtil.getVC();
		String vc = WebUtil.getParameter("vc");
		if (!svc.equalsIgnoreCase(vc))
		{
			WebUtil.error("20000");
			return null;
		}
		Map<String, String> map = WebUtil.getParameters();
		Object user = WebUtil.call("PmsService.login", map);
		if (user == null)
		{
			WebUtil.error("用户名或密码错误！");
			return null;
		}
		WebUtil.setCurrentUser(user);
		loadResandDic();
		WebUtil.ok();
		return null;
	}

	@SuppressWarnings("unchecked")
	public static void loadResandDic()
	{
		WebUtil.attr("res", WebUtil.call("PmsService.userres", WebUtil.getUserid()), "session");
		Map<String, Map<String, Object>> dic = null;
		Object o = WebUtil.attr("dic", "application");
		if (o == null)
		{
			dic = new HashMap<String, Map<String, Object>>();
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

	public String logout()
	{
		WebUtil.clearSession();
		WebUtil.redirect("login.html");
		return null;
	}
	
	public Result welcome()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", WebUtil.getParameter("id"));
		return new Ftl("welcome.ftl", map);
	}
}
