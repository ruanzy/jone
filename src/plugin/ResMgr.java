package plugin;

import javax.servlet.ServletContext;
import org.rzy.task.TaskManager;
import org.rzy.web.Plugin;
import service.PmsService;

public class ResMgr implements Plugin
{
	public void init(ServletContext context)
	{
		context.setAttribute("allres", new PmsService().res());
	}

	public void destroy()
	{
		TaskManager.stop();
	}
}
