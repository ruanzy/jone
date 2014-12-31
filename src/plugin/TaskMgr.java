package plugin;

import org.rzy.task.TaskManager;
import org.rzy.web.Plugin;

public class TaskMgr implements Plugin
{

	public void init()
	{
		TaskManager.start();
	}

	public void destroy()
	{
		TaskManager.stop();
	}

}
