package action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.rzy.mvc.Ftl;
import org.rzy.mvc.Result;
import org.rzy.mvc.XUtil;

public class Common
{
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> menu()
	{
		Object o = XUtil.attr("res", "session");
		List<Map<String, Object>> list = null;
		if (o != null)
		{
			Map<String, List<Map<String, Object>>> userres = (Map<String, List<Map<String, Object>>>) o;
			list = userres.get("menus");
		}
		XUtil.toJSON(list);
		return null;
	}

	@SuppressWarnings("unchecked")
	public String op()
	{
		Object o = XUtil.attr("res", "session");
		List<Map<String, Object>> list = null;
		if (o != null)
		{
			Map<String, List<Map<String, Object>>> userres = (Map<String, List<Map<String, Object>>>) o;
			list = userres.get("ops");
		}
		XUtil.toJSON(list);
		return null;
	}

	@SuppressWarnings("unchecked")
	public String dic()
	{
		String type = XUtil.getParameter("type");
		Map<String, Object> map = null;
		Object o = XUtil.attr("dic", "application");
		if (o != null)
		{
			Map<String, Map<String, Object>> dics = (Map<String, Map<String, Object>>) o;
			map = dics.get(type);
		}
		XUtil.toJSON(map);
		return null;
	}

	public String lineusers()
	{
		Integer numSessions = (Integer) XUtil.attr("numSessions", "application");
		XUtil.toJSON(numSessions);
		return null;
	}

	public String login()
	{
		String svc = XUtil.getVC();
		String vc = XUtil.getParameter("vc");
		if (!svc.equalsIgnoreCase(vc))
		{
			XUtil.error("20000");
			return null;
		}
		Map<String, Object> map = XUtil.getParameterMap();
		Object user = XUtil.call("PmsService.login", map);
		if (user == null)
		{
			XUtil.error("用户名或密码错误！");
			return null;
		}
		XUtil.attr("user", user, "session");
		loadResandDic();
		XUtil.ok();
		return null;
	}

	@SuppressWarnings("unchecked")
	public static void loadResandDic()
	{
		XUtil.attr("res", XUtil.call("PmsService.userres", XUtil.getUserid()), "session");
		Map<String, Map<String, Object>> dic = null;
		Object o = XUtil.attr("dic", "application");
		if (o == null)
		{
			dic = new HashMap<String, Map<String, Object>>();
			List<Map<String, Object>> all = (List<Map<String, Object>>) XUtil.call("PmsService.dics");
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
			XUtil.attr("dic", dic, "application");
		}
	}

	public String logout()
	{
		XUtil.invalidate();
		XUtil.redirect("login.html");
		return null;
	}

	public String vc()
	{
		XUtil.createVC();
		return null;
	}
	
	public Result welcome()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", XUtil.getParameter("id"));
		return new Ftl("welcome.ftl", map);
	}
}
