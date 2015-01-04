package plugin;

import org.rzy.web.Env;
import org.rzy.web.Plugin;

public class LogDir implements Plugin
{

	public void init()
	{
		Env.set("logDir", "D:/Jonelogs");
	}

	public void destroy()
	{
		Env.remove("logDir");
	}

}
