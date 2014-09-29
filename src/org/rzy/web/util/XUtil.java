package org.rzy.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.rzy.log.LogHandler;
import org.rzy.web.Context;
import org.rzy.web.i18n.I18N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class XUtil
{
	static Logger log = LoggerFactory.getLogger(XUtil.class);

	public static Object call(String sid, Object... args)
	{
		log.debug("sid==>" + sid);
		for (int i = 0, len = args.length; i < len; i++)
		{
			log.debug("Parameter" + i + "==>" + args[i]);
		}
		Object result = null;
		String className = StringUtils.substringBeforeLast(sid, ".");
		String methodName = StringUtils.substringAfterLast(sid, ".");
		try
		{
			Class<?> cls = Class.forName("service." + className);
			Object serviceProxy = ServiceProxy.create(cls);
			result = MethodUtils.invokeMethod(serviceProxy, methodName, args);
			boolean flag = Pattern.compile("^(add|del|mod|set|reg|active|cancel)").matcher(methodName).find();
			if (flag)
			{
				StringBuffer logs = new StringBuffer();
				String username = getUsername();
				String requestBody = JSON.toJSONString(args);
				String ip = getIP();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = df.format(new Date());
				logs.append(username).append("|");
				logs.append(ip).append("|");
				logs.append(time).append("|");
				logs.append(sid).append("|");
				logs.append(1).append("|");
				logs.append(requestBody);
				LogHandler.put(logs.toString());
			}
		}
		catch (Exception e)
		{
			if (e instanceof ClassNotFoundException)
			{
				log.debug(e.getMessage() + " Not Found.");
			}
			else if (e instanceof NoSuchMethodException)
			{
				log.debug(e.getMessage());
			}
			else if (e instanceof InvocationTargetException)
			{
				Throwable t = e.getCause();
				log.debug(t.getMessage());
			}
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
		return result;
	}

	public static Object getUser()
	{
		if (Context.getSession() != null)
		{
			return Context.getSession().getAttribute("user");
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static String getUsername()
	{
		Object obj = getUser();
		return obj == null ? null : String.valueOf(((Map<String, String>) obj).get("username"));
	}

	@SuppressWarnings("unchecked")
	public static String getUserid()
	{
		Object obj = getUser();
		return obj == null ? null : String.valueOf(((Map<String, String>) obj).get("id"));
	}

	protected static String getIP()
	{
		return Context.getRequest().getRemoteAddr();
	}

	public static List<Map<String, Object>> toList(String str)
	{
		List<Map<String, Object>> data = null;
		try
		{
			data = JSON.parseObject(str, new TypeReference<List<Map<String, Object>>>()
			{
			});
		}
		catch (Exception e)
		{
			throw new RuntimeException(e.getMessage(), e.getCause());
		}
		return data;
	}

	public static void ok()
	{
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("msg", null);
		try
		{
			HttpServletResponse response = Context.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JSON.toJSONString(result));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void ok(String msg, Object... args)
	{
		// msg = Resource.get(msg)!=null?Resource.get(msg):msg;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", true);
		result.put("msg", msg);
		try
		{
			HttpServletResponse response = Context.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JSON.toJSONString(result));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void error(String msg, Object... args)
	{
		msg = I18N.get(msg) != null ? I18N.get(msg) : msg;
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		result.put("msg", msg);
		try
		{
			HttpServletResponse response = Context.getResponse();
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(JSON.toJSONString(result));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void attr(String key, Object value, String scope)
	{
		if ("application".equals(scope))
		{
			Context.getServletContext().setAttribute(key, value);
		}
		else if ("session".equals(scope))
		{
			Context.getSession().setAttribute(key, value);
		}
		else
		{
			Context.getRequest().setAttribute(key, value);
		}
	}

	public static Object attr(String key, String scope)
	{
		if ("application".equals(scope))
		{
			return Context.getServletContext().getAttribute(key);
		}
		else if ("session".equals(scope))
		{
			return Context.getSession().getAttribute(key);
		}
		else
		{
			return Context.getRequest().getAttribute(key);
		}
	}

	public static void redirect(String url)
	{
		Context.redirect(url);
	}

	public static void forward(String url)
	{
		Context.forward(url);
	}

	public static void invalidate()
	{
		Context.getSession().invalidate();
	}

	public static void createVC()
	{
		int width = 60;
		int height = 20;
		String rchars = "1234567890ABCDEFGHJKLMNPQRSTUVWXYZ";
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(getRandomColor(210, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Courier New", Font.BOLD, 18));
		g.drawRect(0, 0, width - 1, height - 1);

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
			g.drawString(rand, 13 * i + 5, 16);
		}
		g.dispose();
		XUtil.attr("vc", randomStr.toLowerCase(), "session");
		try
		{
			ImageIO.write(img, "JPEG", Context.getResponse().getOutputStream());
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

	public static String getVC()
	{
		return (String) XUtil.attr("vc", "session");
	}

	public static void main(String[] args)
	{
		String text = I18N.get("20000");
		System.out.println(text);
	}
}