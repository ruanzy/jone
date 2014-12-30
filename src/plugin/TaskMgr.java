package plugin;

import javax.servlet.ServletContext;
import org.rzy.task.TaskManager;
import org.rzy.web.Plugin;

public class TaskMgr implements Plugin
{

	public void init(ServletContext context)
	{
		TaskManager.start();
	}

	public void destroy()
	{
		TaskManager.stop();
	}

}
