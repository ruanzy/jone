package action;

import java.util.List;
import java.util.Map;
import com.rz.util.JSONUtil;
import com.rz.util.WebUtil;
import com.rz.web.View;
import com.rz.web.view.Json;
import com.rz.web.view.Msg;

public class Goods
{
	public View category()
	{
		return new Json(WebUtil.call("GoodsService.category"));
	}
	
	public View unitlist()
	{
		return new Json(WebUtil.call("GoodsService.unitlist"));
	}
	
	@SuppressWarnings("unchecked")
	public View unitchanged()
	{
		String apiid = WebUtil.getParameter("apiid");
		String data = WebUtil.getParameter("changed");
		Map<String, Object> map = JSONUtil.toMap(data);
		List<Map<String, Object>> inserted = (List<Map<String, Object>>)map.get("inserted");
		//List<Map<String, Object>> deleted = (List<Map<String, Object>>)map.get("deleted");
		List<Map<String, Object>> updated = (List<Map<String, Object>>)map.get("updated");
		for (Map<String, Object> m : inserted)
		{
			m.put("apiid", apiid);
			WebUtil.call("GoodsService.unitadd", m);
		}
		for (Map<String, Object> m : updated)
		{
			m.put("apiid", apiid);
			WebUtil.call("GoodsService.unitmod", m);
		}
		return new Msg("save success");
	}
	
	public View list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("GoodsService.list", map));
	}
	
	public View add()
	{
		Map<String, String> map = WebUtil.getParameters();
		WebUtil.call("GoodsService.add", map);
		return new Msg("add success");
	}
	
	@SuppressWarnings("unchecked")
	public View changed()
	{
		String apiid = WebUtil.getParameter("apiid");
		String data = WebUtil.getParameter("changed");
		Map<String, Object> map = JSONUtil.toMap(data);
		List<Map<String, Object>> inserted = (List<Map<String, Object>>)map.get("inserted");
		//List<Map<String, Object>> deleted = (List<Map<String, Object>>)map.get("deleted");
		List<Map<String, Object>> updated = (List<Map<String, Object>>)map.get("updated");
		for (Map<String, Object> m : inserted)
		{
			m.put("apiid", apiid);
			WebUtil.call("WarehouseService.add", m);
		}
		for (Map<String, Object> m : updated)
		{
			m.put("apiid", apiid);
			WebUtil.call("WarehouseService.mod", m);
		}
		return new Msg("save success");
	}
}
