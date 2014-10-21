package org.rzy.web;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JOne implements Filter
{
	private ServletContext context;
	static Logger log = LoggerFactory.getLogger(Filter.class);

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		Context rc = Context.begin(this.context, request, response);
		String url = request.getServletPath();
		try
		{
			long t1 = System.currentTimeMillis();
			boolean loginpage = Pattern.compile("login.(jsp|html|htm)$").matcher(url).find();
			boolean extension = url.lastIndexOf(".") != -1;
			boolean page = Pattern.compile("(.jsp|.html|.htm)$").matcher(url).find();
			if (loginpage || extension && !page)
			{
				chain.doFilter(Context.getRequest(), Context.getResponse());
				return;
			}

			// boolean nologin =
			// Pattern.compile("(captcha|common/login|common/logout)$").matcher(url).find();
			// if (!nologin)
			// {
			// Object user = request.getSession().getAttribute("user");
			// if (user == null)
			// {
			// if (Context.isAjax())
			// {
			// response.sendError(1111);
			// }
			// else
			// {
			// response.setCharacterEncoding("UTF-8");
			// String script = "<script>alert('" + XUtil.get("10000")
			// + "');document.location='login.html';</script>";
			// response.getWriter().println(script);
			// }
			// return;
			// }
			// }
			if (page)
			{
				chain.doFilter(Context.getRequest(), Context.getResponse());
				return;
			}
			String[] parts = url.substring(1).split("/");
			if (parts.length < 1)
			{
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			String pck_name = "action";
			String _action_name = parts[0];
			String action_name = Character.toTitleCase(_action_name.charAt(0)) + _action_name.substring(1);
			String action_method_name = (parts.length > 1) ? parts[1] : "execute";
			if ("Captcha".equals(action_name))
			{
				pck_name = "org.rzy.web.action";
			}
			Class<?> cls = Class.forName(pck_name + "." + action_name);
			Object[] ps = new Object[] { url, pck_name + "." + action_name, action_method_name };
			log.debug("url={}, action={}, method={}", ps);
			// Object result = MethodUtils.invokeMethod(cls.newInstance(),
			// action_method_name, null);
			Method method = cls.getMethod(action_method_name);
			Object result = method.invoke(cls.newInstance());
			if (result instanceof Result)
			{
				((Result) result).render();
			}
			if (result instanceof String)
			{
				String path = result.toString();
				if (path.startsWith("redirect:"))
				{
					String basePath = request.getScheme() + "://" + request.getServerName() + ":"
							+ request.getServerPort() + request.getContextPath() + "/";
					response.sendRedirect(basePath + url);
				}
				else
				{
					RequestDispatcher rd = request.getRequestDispatcher(path);
					rd.forward(request, response);
				}
			}
			long t2 = System.currentTimeMillis();
			log.debug("time=" + (t2 - t1) + "ms");
			log.debug("----------");
		}
		catch (Exception e)
		{
			if (e instanceof ClassNotFoundException)
			{
				e.printStackTrace();
			}
			else if (e instanceof NoSuchMethodException)
			{
				e.printStackTrace();
			}
			else if (e instanceof InvocationTargetException)
			{
				Throwable t = e.getCause();
				log.debug(t.getMessage());
				t.printStackTrace();
				if (Context.isAjax())
				{
					response.setStatus(9999);
					response.getWriter().print(t.getMessage());
				}
			}
			else
			{
				throw new ServletException(e);
			}
		}
		finally
		{
			if (rc != null)
			{
				rc.end();
			}
		}
	}

	public void destroy()
	{

	}

	public void init(FilterConfig cfg) throws ServletException
	{
		this.context = cfg.getServletContext();
	}
}