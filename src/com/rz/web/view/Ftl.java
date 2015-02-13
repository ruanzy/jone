package com.rz.web.view;

import java.util.Map;
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

	public void render(ActionContext ac)
	{
		try
		{
			cfg.setServletContextForTemplateLoading(ac.getServletContext(), "ftl");
			ac.getResponse().setContentType("text/html;charset=UTF-8");
			Template t = cfg.getTemplate(ftl, "UTF-8");
			t.process(map, ac.getResponse().getWriter());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
