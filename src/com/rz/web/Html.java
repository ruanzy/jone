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

public class Html implements View
{
	private static Configuration conf = new Configuration();
	private String path;
	
	static{		
		ServletContext servletContext = ActionContext.getServletContext();
		HttpServletRequest request = ActionContext.getRequest();
		HttpServletResponse response = ActionContext.getResponse();
		final String lang = getLang(request);
		response.setContentType("text/html;charset=UTF-8");
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

	public Html(String path)
	{
		this.path = path;
	}

	public void handle()
	{
		try
		{
			HttpServletResponse response = ActionContext.getResponse();
			Template t = conf.getTemplate(path, "UTF-8");
			Map<String, Object> data = ActionContext.getScopeMap();
			t.process(data, response.getWriter());
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
