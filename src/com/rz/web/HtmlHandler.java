package com.rz.web;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.rz.util.I18N;
import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class HtmlHandler
{
	private static Configuration conf = new Configuration();

	public static void handle(ServletContext context, HttpServletRequest request, HttpServletResponse response)
	{
		final String lang = WebKit.getLang(request);
		String fn = request.getServletPath().substring(1);
		try
		{
			response.setContentType("text/html;charset=UTF-8");
			Template t = conf.getTemplate(fn, "UTF-8");
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
			Map<String, Object> data = WebKit.getScopeMap(context, request);
			t.process(data, response.getWriter());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
