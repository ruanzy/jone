package org.rzy.web.result;

import java.util.Map;
import org.rzy.web.Result;
import org.rzy.web.WebUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class Ftl implements Result
{
	static Configuration cfg = new Configuration();

	static
	{
		WebUtil.Response.setContentType("text/html;charset=UTF-8");
		cfg.setDefaultEncoding("UTF-8");
		cfg.setServletContextForTemplateLoading(WebUtil.Application.get(), "ftl");
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
			Template t = null;
			t = cfg.getTemplate(ftl, "UTF-8");
			t.process(map, WebUtil.Response.getWriter());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
