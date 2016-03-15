package com.rz.web.view;

import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import com.rz.web.ActionContext;
import com.rz.web.View;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class Ftl implements View
{
	static Configuration cfg = new Configuration();

	static
	{
		cfg.setDefaultEncoding("UTF-8");
	}

	String ftl;

	Map<String, Object> map;

	public Ftl(String ftl, Map<String, Object> map)
	{
		this.ftl = ftl;
		this.map = map;
	}

	public void handle()
	{
		try
		{
			ServletContext servletContext = ActionContext.getServletContext();
			HttpServletResponse response = ActionContext.getResponse();
			cfg.setServletContextForTemplateLoading(servletContext, "ftl");
			response.setContentType("text/html;charset=UTF-8");
			Template t = cfg.getTemplate(ftl, "UTF-8");
			t.process(map, response.getWriter());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

