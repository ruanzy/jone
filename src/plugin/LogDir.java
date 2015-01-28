package plugin;

import javax.servlet.ServletContext;
import com.rz.web.Env;
import com.rz.web.Plugin;

public class LogDir implements Plugin
{

	public void init(ServletContext context)
	{
		Env.set("logDir", "D:/Jonelogs");
	}

	public void destroy()
	{
		Env.remove("logDir");
	}

}
