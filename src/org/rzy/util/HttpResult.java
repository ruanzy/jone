package org.rzy.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.cookie.Cookie;

public class HttpResult
{
	private List<Cookie> cookies;
	private HttpEntity httpEntity;
	private Map<String, String> allHeader;
	private int statusCode;
	private String body;

	public List<Cookie> getCookies()
	{
		return cookies;
	}

	public void setCookies(List<Cookie> cookies)
	{
		this.cookies = cookies;
	}

	public int getStatusCode()
	{
		return statusCode;
	}

	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}

	public void setHeaders(Header[] headers)
	{
		allHeader = new HashMap<String, String>();
		for (Header header : headers)
		{
			allHeader.put(header.getName(), header.getValue());
		}
	}

	public Map<String, String> getHeaders()
	{
		return allHeader;
	}

	public void setHttpEntity(HttpEntity entity)
	{
		this.httpEntity = entity;
	}

	public HttpEntity getHttpEntity()
	{
		return httpEntity;
	}

	public String getBody()
	{
		return body;
	}

	public void setBody(String body)
	{
		this.body = body;
	}
}
