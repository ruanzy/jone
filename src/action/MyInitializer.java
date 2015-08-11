package action;

import javax.servlet.ServletContext;
import service.PmsService;
import com.rz.task.TaskMgr;
import com.rz.web.Starter;

public class MyInitializer implements Starter
{

	public void start(ServletContext context)
	{
		System.setProperty("logs", "D:/jone/logs");
		TaskMgr.getInstance().start();
		context.setAttribute("allres", new PmsService().res());
	}

}
