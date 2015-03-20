package action;

import java.util.List;
import java.util.Map;
import com.rz.util.JSONUtil;
import com.rz.util.WebUtil;
import com.rz.web.View;
import com.rz.web.view.Json;
import com.rz.web.view.Msg;

public class Api
{
	public View params()
	{
		String apiid = WebUtil.getParameter("apiid");
		Object obj = WebUtil.call("ApiService.find", apiid);
		return new Json(obj);
	}
	
	@SuppressWarnings("unchecked")
	public View changed()
	{
		String apiid = WebUtil.getParameter("apiid");
		String data = WebUtil.getParameter("changed");
		Map<String, Object> map = JSONUtil.toMap(data);
		List<Map<String, Object>> inserted = (List<Map<String, Object>>)map.get("inserted");
		List<Map<String, Object>> deleted = (List<Map<String, Object>>)map.get("deleted");
		List<Map<String, Object>> updated = (List<Map<String, Object>>)map.get("updated");
		for (Map<String, Object> m : inserted)
		{
			m.put("apiid", apiid);
			WebUtil.call("ApiService.add", m);
		}
		for (Map<String, Object> m : deleted)
		{
			String id = m.get("id").toString();
			WebUtil.call("ApiService.del", id);
		}
		for (Map<String, Object> m : updated)
		{
			m.put("apiid", apiid);
			WebUtil.call("ApiService.mod", m);
		}
		return new Msg("save success");
	}
}
