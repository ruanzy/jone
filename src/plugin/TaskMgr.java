package plugin;

import javax.servlet.ServletContext;
import com.rz.task.TaskManager;
import com.rz.web.Plugin;

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
