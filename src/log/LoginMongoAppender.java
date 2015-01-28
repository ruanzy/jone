package log;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import com.rz.web.MongoAppender;

public class LoginMongoAppender extends MongoAppender
{

	@Override
	public Map<String, String> MsgParse(String msg)
	{
		Map<String, String> map = new HashMap<String, String>();
		String[] arr = msg.split("\\|");
		map.put("user", arr[0]);
		map.put("ip", arr[1]);
		String ua = arr[2];
		map.put("os", OS(ua));
		map.put("browser", Browser(ua));
		return map;
	}
	
	private String OS(String userAgent)
	{
		if(userAgent.indexOf("Windows NT 6.4")!=-1){
			return "Windows 10";
		}
		if(userAgent.indexOf("Windows NT 6.3")!=-1){
			return "Windows 8.1";
		}
		if(userAgent.indexOf("Windows NT 6.2")!=-1){
			return "Windows 8";
		}
		if(userAgent.indexOf("Windows NT 6.1")!=-1){
			return "Windows 7";
		}
		if(userAgent.indexOf("Windows NT 6.0")!=-1){
			return "Windows VISTA";
		}
		if(userAgent.indexOf("Windows NT 5.1")!=-1){
			return "Windows XP";
		}
		return "unknown";
	}
	
	private String Browser(String userAgent)
	{
		String ua = userAgent.toLowerCase();
		Pattern safari = Pattern.compile("webkit");
		Pattern opera = Pattern.compile("opera");
		Pattern msie = Pattern.compile("msie");
		Pattern firefox = Pattern.compile("firefox");
		Pattern chrome = Pattern.compile("chrome");
		Pattern compatible = Pattern.compile("(compatible|webkit)");
		if (chrome.matcher(ua).find())
		{
			return "chrome";
		}
		if (safari.matcher(ua).find())
		{
			return "safari";
		}
		if (opera.matcher(ua).find())
		{
			return "opera";
		}
		if (msie.matcher(ua).find() && !opera.matcher(ua).find())
		{
			return "msie";
		}
		if (firefox.matcher(ua).find() && !compatible.matcher(ua).find())
		{
			return "firefox";
		}
		return "unknown";
	}

}
