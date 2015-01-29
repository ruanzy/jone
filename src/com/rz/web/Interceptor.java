package com.rz.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.rz.annotation.InterceptorMapping;

public abstract class Interceptor
{
	private final static String checkReg = "^((/\\w+)*)[/]?[\\*]?";
	private Matcher matcher = null;

	boolean initMatcher()
	{
		InterceptorMapping im = this.getClass().getAnnotation(InterceptorMapping.class);
		if (im == null)
		{
			return false;
		}
		String[] urlPattern = im.urls();
		matcher = Pattern.compile(checkReg).matcher("");
		StringBuilder icptPattern = new StringBuilder();
		for (String url : urlPattern)
		{
			matcher.reset(url);
			if (matcher.matches())
			{
				if (url.endsWith("*"))
				{
					icptPattern.append("|").append(matcher.group(1)).append("[/][\\S]+");
				}
				else
				{
					icptPattern.append("|").append(matcher.group(1)).append("[/]?");
				}
			}
			else
			{
			}
		}
		if (icptPattern.length() > 1)
		{
			icptPattern.deleteCharAt(0);
			matcher.usePattern(Pattern.compile(icptPattern.toString()));
		}
		else
		{
			return false;
		}
		return true;
	}

	boolean matchers(String url)
	{
		return matcher.reset(url).matches();
	}

	public abstract void intercept(ActionHandler ah, InterceptorChain chain) throws Exception;
}