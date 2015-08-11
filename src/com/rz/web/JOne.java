package com.rz.web;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.rz.util.I18N;
import com.rz.web.interceptor.Interceptors;
import freemarker.template.Configuration;
import freemarker.template.SimpleScalar;
import freemarker.template.Template;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class JOne implements Filter
{
	private ServletContext context;
	private static Configuration conf = new Configuration();
	private Starter starter;

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String url = request.getServletPath();
		request.setCharacterEncoding("UTF-8");
		boolean isStatic = (url.lastIndexOf(".") != -1);
		boolean isHtml = url.endsWith(".html") || url.endsWith(".htm");
		try
		{
			if (isStatic && !isHtml)
			{
				chain.doFilter(request, response);
				return;
			}
			if (isHtml)
			{
				final String lang = WebKit.getLang(request);
				String fn = url.substring(1);
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
				return;
			}
			ActionContext ac = ActionContext.create(context, request, response);
			new ActionInvocation(ac).invoke();
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
		finally
		{
			ActionContext.destroy();
		}
	}

	public void destroy()
	{
	}

	public void init(FilterConfig cfg) throws ServletException
	{
		String _starter = cfg.getInitParameter("starter");
		try
		{
			this.context = cfg.getServletContext();
			StringBuffer sb = new StringBuffer();
			sb.append("*************************************").append("\r\n");
			sb.append("**                                 **").append("\r\n");
			sb.append("**          JOne Satrting...       **").append("\r\n");
			sb.append("**                                 **").append("\r\n");
			sb.append("*************************************");
			System.out.println(sb);
			if (_starter != null)
			{
				Class<?> startercls = Class.forName(_starter);
				this.starter = (Starter) (startercls.newInstance());
				this.starter.start(this.context);
			}
			Actions.init();
			Interceptors.init();
			conf.setServletContextForTemplateLoading(this.context, "/");
			conf.setDefaultEncoding("UTF-8");
		}
		catch (Exception e)
		{
			throw new ServletException(e);
		}
	}
}