package org.rzy.web;

import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalServiceCaller implements ServiceCaller
{
	static Logger log = LoggerFactory.getLogger(LocalServiceCaller.class);

	public Object call(String sid, Object... args)
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
		log.debug(sb.toString());
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
				error = "业务处理接口" + e.getMessage() + " Not Found!";
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

}
