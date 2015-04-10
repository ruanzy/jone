package action;

import javax.servlet.ServletContext;
import service.PmsService;
import com.rz.schedule.Schedules;
import com.rz.web.Initializer;
import com.rz.web.interceptor.Interceptors;

public class MyInitializer implements Initializer
{

	public void init(ServletContext context)
	{
		System.setProperty("logs", "D:/jone/logs");
		Schedules.init();
		Interceptors.init();
		context.setAttribute("allres", new PmsService().res());
	}

}
