package plugin;

import javax.servlet.ServletContext;
import org.rzy.web.Env;
import org.rzy.web.Plugin;

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
