package action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.rz.web.View;
import com.rz.web.view.Json;

public class Api
{
	public View params()
	{
		List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("pname", "username");
		p.put("required", "0");
		p.put("memo", "用户名");
		params.add(p);
		return new Json(params);
	}
}
