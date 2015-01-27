package org.rzy.web.result;

import java.util.Map;
import org.rzy.web.ActionContext;
import org.rzy.web.Result;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class Ftl implements Result
{
	static Configuration cfg = new Configuration();

	static
	{
		cfg.setDefaultEncoding("UTF-8");
		cfg.setServletContextForTemplateLoading(ActionContext.getActionContext().getServletContext(), "ftl");
	}

	String ftl;

	Map<String, Object> map;

	public Ftl(String ftl, Map<String, Object> map)
	{
		this.ftl = ftl;
		this.map = map;
	}

	public void render()
	{
		try
		{
			ActionContext.getActionContext().getHttpServletResponse().setContentType("text/html;charset=UTF-8");
			Template t = cfg.getTemplate(ftl, "UTF-8");
			t.process(map, ActionContext.getActionContext().getHttpServletResponse().getWriter());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
