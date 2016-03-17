package action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rz.util.JSONUtil;
import com.rz.util.WebUtil;
import com.rz.web.Json;
import com.rz.web.View;
import com.rz.web.view.Ftl;
import com.rz.web.view.Msg;

public class Receive
{
	public View pager()
	{
		Map<String, String> map = WebUtil.getParameters();
		Object obj = WebUtil.call("ReceiveService.pager", map);
		return new Json(obj);
	}
	
	public View detailview()
	{
		String no = WebUtil.getParameter("no");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("no", no);
		return new Ftl("receivedetail.html", map);
	}
	
	public View record()
	{
		String no = WebUtil.getParameter("no");
		Map<String, Object> map = new HashMap<String, Object>();
		String user = WebUtil.getUser();
		map.put("user", user);
		map.put("no", no);
		return new Ftl("receiverecord.html", map);
	}
	
	public View recordadd()
	{
		Map<String, String> map = WebUtil.getParameters();
		WebUtil.call("ReceiveService.recordadd", map);
		return new Msg("save success");
	}
	
	public View detaillist()
	{
		String no = WebUtil.getParameter("no");
		Object data = WebUtil.call("ReceiveService.detailllist", no);
		return new Json(data);
	}
	
	public View receivablepager()
	{
		Map<String, String> map = new HashMap<String, String>();
		Object obj = WebUtil.call("FundService.receivablepager", map);
		return new Json(obj);
	}
	
	@SuppressWarnings("unchecked")
	public View payablechanged()
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
	
	@SuppressWarnings("unchecked")
	public View receivablechanged()
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
