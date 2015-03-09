package action;

import javax.servlet.ServletContext;
import service.PmsService;
import com.rz.web.Initializer;

public class MyInitializer implements Initializer
{

	public void init(ServletContext context)
	{
		context.setAttribute("allres", new PmsService().res());
	}

}
