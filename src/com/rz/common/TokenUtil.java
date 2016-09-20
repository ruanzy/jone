package com.rz.common;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.codec.digest.DigestUtils;

public class TokenUtil
{
	static final long DIFF = 60 * 60 * 2;
	static Map<String, Token> tokenStore = new ConcurrentHashMap<String, Token>();
	static ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
	static
	{
		service.scheduleAtFixedRate(new Runnable()
		{
			public void run()
			{
				long t1 = new Date().getTime();
				for (Map.Entry<String, Token> entry : tokenStore.entrySet())
				{
					long t2 = entry.getValue().getTime();
					if (t2 - t1 > DIFF)
					{
						tokenStore.remove(entry.getKey());
					}
				}
			}
		}, DIFF, DIFF, TimeUnit.SECONDS);
	}

	public static String generatorToken(String signature)
	{
		Date time = new Date();
		try
		{
			Token t = new Token(signature, time.getTime());
			byte[] b = (time + DigestUtils.md5Hex(signature)).getBytes("UTF-8");
			String token = DigestUtils.md5Hex(b);
			tokenStore.put(token, t);
			return token;
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return signature;
	}

	public static Token getToken(String token)
	{
		if (tokenStore.containsKey(token))
		{
			return tokenStore.get(token);
		}
		return null;
	}

	public static String getUser(String token)
	{
		if (token == null)
		{
			return null;
		}
		if (tokenStore.containsKey(token))
		{
			return tokenStore.get(token).getUsername();
		}
		return null;
	}

	public static void removeToken(String token)
	{
		if (token == null)
			return;
		if (tokenStore.containsKey(token))
		{
			tokenStore.remove(token);
		}
	}

	public static boolean validateToken(String token)
	{
		if (token == null)
			return false;
		if (tokenStore.containsKey(token))
		{
			Token t = tokenStore.get(token);
			Date normal = new Date();
			if (normal.getTime() - t.getTime() > DIFF * 1000)
			{
				tokenStore.remove(token);
				return false;
			}
			else
			{
				t.setTime(normal.getTime());
			}
		}
		else
		{
			return false;
		}
		return true;
	}
}

class Token
{
	String username;
	long time;

	public Token(String username, long time)
	{
		this.username = username;
		this.time = time;
	}

	public String getUsername()
	{
		return username;
	}

	public void setTime(long time)
	{
		this.time = time;
	}

	public long getTime()
	{
		return time;
	}
}
