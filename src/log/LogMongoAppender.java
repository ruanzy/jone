package log;

import java.util.HashMap;
import java.util.Map;
import com.rz.web.MongoAppender;

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
		//map.put("args", "");
		map.put("memo", arr[4]);
		map.put("result", arr[5]);
		return map;
	}

}
