package org.rzy.web;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import net.sf.cglib.proxy.NoOp;

import org.rzy.dao.Dao;

import com.alibaba.fastjson.JSON;

class ServiceProxy
{
	static String express = "^(add|del|mod|set|reg|active|cancel)";
	static MethodInterceptor interceptor = new MethodInterceptor()
	{
		public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable
		{
			Object result = null;
			Dao dao = Dao.getInstance();
			try
			{
				dao.begin();
				result = methodProxy.invokeSuper(obj, args);
				dao.commit();
				StringBuffer logs = new StringBuffer();
				String username = WebUtil.getUser().get("username");
				String requestBody = JSON.toJSONString(args);
				String ip = WebUtil.getIP();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time = df.format(new Date());
				String pcls = obj.getClass().getSimpleName();
				String sid = pcls.split("\\$\\$")[0] + "." + method.getName();
				logs.append(username).append("|");
				logs.append(ip).append("|");
				logs.append(time).append("|");
				logs.append(sid).append("|");
				logs.append(1).append("|");
				logs.append(requestBody);
				writeLog(logs.toString());
			}
			catch (Exception e)
			{
				dao.rollback();
				throw e;
			}
			return result;
		}
	};

	static CallbackFilter filter = new CallbackFilter()
	{
		public int accept(Method arg0)
		{
			return Pattern.compile(express).matcher(arg0.getName()).find() ? 0 : 1;
		}
	};

	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> cls)
	{
		try
		{
			Enhancer en = new Enhancer();
			en.setSuperclass(cls);
			en.setCallbacks(new Callback[] { interceptor, NoOp.INSTANCE });
			en.setCallbackFilter(filter);
			return (T) en.create();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}	
	
	private static void writeLog(String log)
	{
		String sql = "insert into log(id,operator,ip,time,method,result,memo) values(?,?,?,?,?,?,?)";
		Dao dao = Dao.getInstance();
		try
		{
			dao.begin();
			int id = dao.getID("log");
			String[] arr = log.split("\\|");
			Object[] params = new Object[] { id, arr[0], arr[1], arr[2], arr[3], arr[4], arr[5] };
			dao.update(sql, params);
			dao.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			dao.rollback();
		}
	}
}