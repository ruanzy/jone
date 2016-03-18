package com.rz.web;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class Segment implements View
{
	private static Configuration conf = new Configuration();
	private String path;
	private Map<String, Object> data;

	static
	{
		ServletContext servletContext = ActionContext.getServletContext();
		HttpServletRequest request = ActionContext.getRequest();
		HttpServletResponse response = ActionContext.getResponse();
		response.setCharacterEncoding("UTF-8");
		final String lang = getLang(request);
		conf.setServletContextForTemplateLoading(servletContext, "/");
		conf.setSharedVariable("i18n", new TemplateMethodModel()
		{
			@SuppressWarnings("rawtypes")
			public Object exec(List list) throws TemplateModelException
			{
				String key = (String) list.get(0);
				Locale locale = new Locale(lang);
				String value = I18N.get(locale, key);
				return new SimpleScalar(value);
			}
		});
	}

	public Segment(String path)
	{
		this.path = path;
	}

	public Segment(String path, Map<String, Object> data)
	{
		this.path = path;
		this.data = data;
	}

	public void handle()
	{
		try
		{
			HttpServletResponse response = ActionContext.getResponse();
			response.setCharacterEncoding("UTF-8");
			Template t = conf.getTemplate(path, "UTF-8");
			Map<String, Object> scope = ActionContext.getScopeMap();
			if (data != null)
			{
				scope.putAll(data);
			}
			t.process(scope, response.getWriter());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static String getLang(HttpServletRequest request)
	{
		String lang = "zh";
		Cookie[] cks = request.getCookies();
		if (cks != null)
		{
			for (Cookie cookie : cks)
			{
				if ("lang".equals(cookie.getName()))
				{
					lang = cookie.getValue();
					break;
				}
			}
		}
		return lang;
	}
}
