package log;

import java.util.HashMap;
import java.util.Map;
import com.rz.web.MongoAppender;

public class JvmMongoAppender extends MongoAppender
{

	@Override
	public Map<String, String> MsgParse(String msg)
	{
		Map<String, String> map = new HashMap<String, String>();
		String[] arr = msg.split(" ");
		map.put("memUsed", arr[0]);
		map.put("cpuUsed", arr[1]);
		map.put("threadCount", arr[2]);
		return map;
	}

}
