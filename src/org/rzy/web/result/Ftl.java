package org.rzy.web.result;

import java.util.Map;

import org.rzy.web.Result;
import org.rzy.web.WebUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class Ftl implements Result
{
	String ftl;

	Map<String, Object> map;

	public Ftl(String ftl, Map<String, Object> map)
	{
		this.ftl = ftl;
		this.map = map;
	}

	public void render()
	{
		WebUtil.getResponse().setContentType("text/html;charset=UTF-8");
		Template t = null;
		Configuration cfg = new Configuration();
		cfg.setDefaultEncoding("UTF-8");
		cfg.setServletContextForTemplateLoading(WebUtil.getServletContext(), "ftl");
		try
		{
			t = cfg.getTemplate(ftl, "UTF-8");
			t.process(map, WebUtil.getResponse().getWriter());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
