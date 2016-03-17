package action;

import java.util.HashMap;
import java.util.Map;
import com.rz.util.JSONUtil;
import com.rz.util.WebUtil;
import com.rz.web.Json;
import com.rz.web.View;
import com.rz.web.view.Ftl;
import com.rz.web.view.Msg;

public class Purchase
{
	public View list()
	{
		Map<String, String> map = WebUtil.getParameters();
		return new Json(WebUtil.call("PurchaseService.list", map));
	}
	
	public View detailview()
	{
		String purchasebill = WebUtil.getParameter("purchasebill");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("purchasebill", purchasebill);
		return new Ftl("pdetail.html", map);
	}
	
	public View detaillist()
	{
		String purchasebill = WebUtil.getParameter("purchasebill");
		Object detaillist = WebUtil.call("PurchaseService.detaillist", purchasebill);
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("detaillist", detaillist);
//		return new Ftl("pdetail.ftl", map);
		return new Json(detaillist);
	}

	public View tobilladd()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String user = WebUtil.getUser();
		map.put("user", user);
		return new Ftl("billadd.html", map);
	}

	public View income()
	{
		String no = WebUtil.getParameter("no");
		WebUtil.call("PurchaseService.income", no);
		return new Msg("income success");
	}

	public View add()
	{
		String bill = WebUtil.getParameter("bill");
		Map<String, Object> map = JSONUtil.toMap(bill);
		WebUtil.call("PurchaseService.add", map);
		return new Msg("save success");
	}

	public View del()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("PmsService.deluser", ids);
		return new Msg("del success");
	}

	public String active()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("PmsService.activeuser", ids);
		return null;
	}

	public String cancel()
	{
		String ids = WebUtil.getParameter("ids");
		WebUtil.call("PmsService.canceluser", ids);
		return null;
	}

	public View tosetrole()
	{
		String user = WebUtil.getParameter("user");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("user", WebUtil.getParameter("user"));
		map.put("roles", WebUtil.call("PmsService.userrole", user));
		return new Ftl("setrole.ftl", map);
	}

	public View allrole()
	{
		return new Json(WebUtil.call("PmsService.allrole"));
	}

	public View assignedroles()
	{
		String user = WebUtil.getParameter("user");
		return new Json(WebUtil.call("PmsService.assignedroles", user));
	}

	public View setrole()
	{
		String user = WebUtil.getParameter("user");
		String roles = WebUtil.getParameter("roles");
		WebUtil.call("PmsService.setrole", user, roles);
		return new Msg("设置角色ok...");
	}
}
