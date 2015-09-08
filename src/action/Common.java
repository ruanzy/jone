package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;
import com.rz.util.ServerInfo;
import com.rz.util.Uploader;
import com.rz.util.WebUtil;
import com.rz.web.View;
import com.rz.web.view.Ftl;
import com.rz.web.view.Json;
import com.rz.web.view.Jsonp;
import com.rz.web.view.Redirect;
import com.rz.web.view.Text;

public class Common
{
	@SuppressWarnings("unchecked")
	public View menu()
	{
		String user = WebUtil.getUser();
		Object o = null;
		List<Map<String, Object>> res = null;
		List<Map<String, Object>> menus = null;
		if ("admin".equals(user))
		{
			o = WebUtil.Application.attr("allres");
		}
		else
		{
			o = WebUtil.Session.attr("res");
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
	public View op()
	{
		String user = WebUtil.getUser();
		Object o = null;
		List<Map<String, Object>> res = null;
		List<Map<String, Object>> ops = null;
		if ("admin".equals(user))
		{
			o = WebUtil.Application.attr("allres");
		}
		else
		{
			o = WebUtil.Session.attr("res");
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

	public View dic()
	{
		Object o = WebUtil.Application.attr("dic");
		return new Json(o);
	}

	public View res()
	{
		Object o = WebUtil.Application.attr("allres");
		return new Json(o);
	}

	public View lineusers()
	{
		Integer numSessions = (Integer) WebUtil.Application.attr("numSessions");
		return new Json(numSessions);
	}

	@SuppressWarnings("unchecked")
	public static void loadResandDic()
	{
		Object allres = WebUtil.Application.attr("allres");
		if (allres == null)
		{
			WebUtil.Application.attr("allres", WebUtil.call("PmsService.res"));
		}
		Object o = WebUtil.Application.attr("dic");
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
			WebUtil.Application.attr("dic", dic);
		}
	}

	public View logout()
	{
		WebUtil.Session.clear();
		return new Redirect("login.html");
	}

	public View welcome()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", WebUtil.getParameter("id"));
		map = ServerInfo.base();
		return new Json(map);
	}

	public View header()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		String user = WebUtil.getUser();
		data.put("user", user);
		return new Ftl("header.ftl", data);
	}

	public View sidebar()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		String user = WebUtil.getUser();
		Object o = null;
		if ("admin".equals(user))
		{
			o = WebUtil.Application.attr("allres");
		}
		else
		{
			o = WebUtil.Session.attr("res");
		}
		data.put("all", o);
		return new Ftl("sidebar.ftl", data);
	}

	public View center()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		return new Ftl("center.ftl", map);
	}

	public View upload()
	{
		String path = "d:/upload/";
		Uploader uploader;
		uploader = Uploader.prepare(64000000L);
		Map<String, String> parameters = uploader.getParameters();
		List<FileItem> items = uploader.getItems();
		String tmpurl = parameters.get("tmpurl");
		String id = parameters.get("id");
		uploader.upload(path);
		WebUtil.setHeader("Location", tmpurl + "?data=" + "asdasd");
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head><script type='text/javascript'>");
		sb.append("window.parent.callback('");
		sb.append("This data is from server!");
		sb.append("');");
		sb.append("</script></head><body>This data is from server!</body></html>");
		return new Text(sb.toString());
	}

	public View base()
	{
		Map<String, Object> map = ServerInfo.base();
		return new Json(map);
	}

	public View userinfo()
	{
		Map<String, Object> data = new HashMap<String, Object>();
		String user = WebUtil.getUser();
		data.put("user", user);
		return new Ftl("userinfo.ftl", data);
	}

	public View cookies()
	{
		// String callback = WebUtil.getParameter("callback");
		return new Jsonp("callback", WebUtil.Cookies.getAll());
	}
	
	public View userres()
	{
		String user = WebUtil.getParameter("user");
		return new Json(WebUtil.call("PmsService.userres", user));
	}
}