package log;

import java.util.HashMap;
import java.util.Map;
import org.rzy.web.MongoAppender;

public class LogMongoAppender extends MongoAppender
{

	@Override
	public Map<String, String> MsgParse(String msg)
	{
		Map<String, String> map = new HashMap<String, String>();
		String[] arr = msg.split("\\|");
		map.put("user", arr[0]);
		map.put("ip", arr[1]);
		map.put("sid", arr[2]);
		map.put("memo", arr[3]);
		map.put("result", arr[4]);
		return map;
	}

}
