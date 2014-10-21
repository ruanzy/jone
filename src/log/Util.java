package log;

import java.util.List;
import java.util.Map;
import org.rzy.web.WebUtil;

public class Util
{
	@SuppressWarnings("unchecked")
	public static String getOP(String method)
	{
		List<Map<String, Object>> allres = (List<Map<String, Object>>) WebUtil.attr("allres", "application");
		for (Map<String, Object> map : allres)
		{
			String m = String.valueOf(map.get("method"));
			if (method.equals(m))
			{
				return String.valueOf(map.get("name"));
			}
		}
		return null;
	}
}
