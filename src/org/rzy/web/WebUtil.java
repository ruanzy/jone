package org.rzy.web;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtil
{
	private ServletContext servletContext;
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private final static ThreadLocal<WebUtil> webUtil = new ThreadLocal<WebUtil>();
	static Logger log = LoggerFactory.getLogger(WebUtil.class);

	private WebUtil()
	{
	}

	protected static WebUtil init(ServletContext servletContext, HttpServletRequest req, HttpServletResponse res)
			throws UnsupportedEncodingException
	{
		WebUtil wu = new WebUtil();
		wu.servletContext = servletContext;
		wu.request = req;
		wu.response = res;
		wu.session = req.getSession();
		webUtil.set(wu);
		return wu;
	}

	protected void destroy()
	{
		this.servletContext = null;
		this.request = null;
		this.response = null;
		this.session = null;
		webUtil.remove();
	}

	public static Object call(String sid, Object... args)
	{
		String[] arr = sid.split("\\.");
		if (arr.length != 2)
		{
			throw new RuntimeException("业务接口不存在");
		}
		String className = arr[0];
		String methodName = arr[1];
		String fullName = "service." + className;
		StringBuffer sb = new StringBuffer();
		sb.append("service=").append(sid).append("(");
		for (int i = 0, len = args.length; i < len; i++)
		{
			sb.append("arg" + i);
			if (i != len - 1)
			{
				sb.append(",");
			}
		}
		sb.append(")");
		// log.debug(sb.toString());
		for (int i = 0, len = args.length; i < len; i++)
		{
			// log.debug("arg" + i + "=" + JSON.toJSONString(args[i]));
		}
		Object result = null;
		StringBuffer logs = new StringBuffer();
		String user = WebUtil.getUser();
		String ip = WebUtil.getIP();
		// String op = "";//Util.getOP(sid);
		// String requestBody = JSON.toJSONString(args);
		logs.append(user).append("|");
		logs.append(ip).append("|");
		// logs.append(op).append("|");
		logs.append(sid).append("|");
		// logs.append(requestBody).append("|");
		logs.append("").append("|");
		try
		{
			Object proxy = ServiceProxy.get(fullName);
			result = MethodUtils.invokeMethod(proxy, methodName, args);
			logs.append(1);
		}
		catch (Exception e)
		{
			String error = "";
			if (e instanceof ClassNotFoundException || e instanceof NoSuchMethodException)
			{
				error = "业务处理接口未找到";
			}
			else if (e instanceof InvocationTargetException)
			{
				Throwable t = ((InvocationTargetException) e).getTargetException();
				error = t.getMessage();
			}
			logs.append(error).append("|");
			logs.append(0);
			// e.printStackTrace();
			throw new ServiceException(error);
		}
		finally
		{
			log.debug(logs.toString());
		}
		return result;
	}

	public static class Request
	{
		public static HttpServletRequest get()
		{
			return webUtil.get().request;
		}

		public static void setCharacterEncoding(String encoding)
		{
			try
			{
				get().setCharacterEncoding(encoding);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}

		public static void attr(String key, Object value)
		{
			get().setAttribute(key, value);
		}

		public static Object attr(String key)
		{
			return get().getAttribute(key);
		}
	}

	public static class Response
	{
		public static HttpServletResponse get()
		{
			return webUtil.get().response;
		}

		public static void setContentType(String type)
		{
			get().setContentType(type);
		}

		public static void setCharacterEncoding(String encoding)
		{
			get().setCharacterEncoding(encoding);
		}

		public static OutputStream getOutputStream()
		{
			try
			{
				return get().getOutputStream();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}

		public static PrintWriter getWriter()
		{
			try
			{
				return get().getWriter();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}

		public static void write(String txt)
		{
			getWriter().print(txt);
		}
	}

	public static class Session
	{
		public static HttpSession get()
		{
			return webUtil.get().session;
		}

		public static void clear()
		{
			get().invalidate();
		}

		public static void attr(String key, Object value)
		{
			get().setAttribute(key, value);
		}

		public static Object attr(String key)
		{
			return get().getAttribute(key);
		}
	}

	public static class Application
	{
		public static ServletContext get()
		{
			return webUtil.get().servletContext;
		}

		public static void attr(String key, Object value)
		{
			get().setAttribute(key, value);
		}

		public static Object attr(String key)
		{
			return get().getAttribute(key);
		}

		public static void setUserres(String user)
		{
			attr("USERRES", user);
		}

		public static String getUserres()
		{
			Object user = attr("USERRES");
			return (String) user;
		}
	}

	public static String getWebRoot()
	{
		return WebUtil.Application.get().getRealPath("/");
	}

	public static String getMethod()
	{
		return WebUtil.Request.get().getMethod();
	}

	public static Map<String, String> getParameters()
	{
		Map<String, String> ps = null;
		Enumeration<?> em = WebUtil.Request.get().getParameterNames();
		if (em.hasMoreElements())
		{
			ps = new HashMap<String, String>();
			while (em.hasMoreElements())
			{
				String k = (String) em.nextElement();
				String v = getParameter(k);
				ps.put(k, v);
			}
		}
		return ps;
	}

	public static String getParameter(String name)
	{
		return WebUtil.Request.get().getParameter(name);
	}

	public static String getIP()
	{
		return WebUtil.Request.get().getRemoteAddr();
	}

	public static String getHeader(String key)
	{
		return WebUtil.Request.get().getHeader(key);
	}

	public static boolean isAjax()
	{
		String xhr = getHeader("x-requested-with");
		if (xhr != null && xhr.trim().length() > 0)
		{
			return true;
		}
		return false;
	}

	public static boolean isAdmin(String username, String password)
	{
		return ("admin").equals(username) && ("162534").equals(password);
	}

	public static void forward(String url)
	{
		RequestDispatcher rd = WebUtil.Request.get().getRequestDispatcher(url);
		try
		{
			rd.forward(WebUtil.Request.get(), WebUtil.Response.get());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void redirect(String url)
	{
		try
		{
			HttpServletRequest request = WebUtil.Request.get();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() + "/";
			WebUtil.Response.get().sendRedirect(basePath + url);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void setUser(String user)
	{
		WebUtil.Session.attr("RZY_USER", user);
	}

	public static String getUser()
	{
		Object user = WebUtil.Session.attr("RZY_USER");
		String SSOTOKEN = null;
		Cookie[] cookies = WebUtil.Request.get().getCookies();
		if (cookies != null)
		{
			for (int i = 0; i < cookies.length; i++)
			{
				if (cookies[i].getName().equals("SSOTOKEN"))
				{
					SSOTOKEN = cookies[i].getValue();
					break;
				}
			}
		}
		if(SSOTOKEN != null){
			String[] arr = SSOTOKEN.split("_");
			return arr[1];
		}
		return (String) user;
	}

	public static void setCaptcha(String captcha)
	{
		WebUtil.Session.attr("RZY_CAPTCHA", captcha);
	}

	public static String getCaptcha()
	{
		Object captcha = WebUtil.Session.attr("RZY_CAPTCHA");
		return (String) captcha;
	}

	public static void Captcha()
	{
		int width = 75;
		int height = 35;
		String rchars = "1234567890ABCDEFGHJKLMNPQRSTUVWXYZ";
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(getRandomColor(210, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Courier New", Font.BOLD, 20));
		g.drawRect(0, 0, width, height);

		g.setColor(getRandomColor(150, 250));
		Random random = new Random();
		for (int i = 0; i < 155; i++)
		{
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			g.drawLine(x, y, x + x1, y + y1);
		}
		String randomStr = "";
		for (int i = 0; i < 4; i++)
		{
			int len = rchars.length();
			int randomNum = random.nextInt(len);
			String rand = rchars.substring(randomNum, randomNum + 1);
			randomStr += rand;
			g.setColor(new Color(25 + random.nextInt(110), 25 + random.nextInt(110), 25 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 12, height * 2 / 3);
		}
		g.dispose();
		setCaptcha(randomStr.toLowerCase());
		try
		{
			ImageIO.write(img, "JPEG", WebUtil.Response.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static Color getRandomColor(int fc, int bc)
	{
		Random random = new Random();
		if (fc > 255)
		{
			fc = 255;
		}
		if (bc > 255)
		{
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}