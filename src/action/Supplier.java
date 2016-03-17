package action;

import java.util.List;
import java.util.Map;
import com.rz.util.JSONUtil;
import com.rz.util.WebUtil;
import com.rz.web.Json;
import com.rz.web.View;
import com.rz.web.view.Msg;

public class Supplier
{
	public View category()
	{
		return new Json(WebUtil.call("SupplierService.category"));
	}
	
	public View list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("SupplierService.list", map));
	}
	
	public View pager()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("SupplierService.pager", map));
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
			WebUtil.call("SupplierService.add", m);
		}
		for (Map<String, Object> m : updated)
		{
			m.put("apiid", apiid);
			WebUtil.call("SupplierService.mod", m);
		}
		return new Msg("save success");
	}
}
