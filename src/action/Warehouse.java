package action;

import java.util.List;
import java.util.Map;
import com.rz.util.JSONUtil;
import com.rz.util.WebUtil;
import com.rz.web.View;
import com.rz.web.view.Json;
import com.rz.web.view.Msg;

public class Warehouse
{
	public View list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("WarehouseService.list", map));
	}
	
	public View del()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("WarehouseService.del", ids);
		return new Msg("del success");
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
