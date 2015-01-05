package org.rzy.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil
{
	public static Response get(String url, Map<String, String> headers, Map<String, String> params)
	{
		Response res = null;
		CloseableHttpClient client = null;
		try
		{
			BasicCookieStore cookieStore = new BasicCookieStore();
			client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			url = (null == params ? url : url + "?" + parseParam(params));
			HttpGet get = new HttpGet(url);
			for (Map.Entry<String, String> header : headers.entrySet())
			{
				get.setHeader(header.getKey(), header.getValue());
			}
			HttpResponse response = client.execute(get);
			HttpEntity entity = response.getEntity();
			res = new Response();
			res.setCookies(cookieStore.getCookies());
			res.setStatusCode(response.getStatusLine().getStatusCode());
			res.setHeaders(response.getAllHeaders());
			res.setHttpEntity(entity);
			res.setBody(EntityUtils.toString(entity));
			return res;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if (null != client)
			{
				try
				{
					client.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static Response post(String url, Map<String, String> headers, Map<String, String> params)
	{
		CloseableHttpClient client = null;
		Response res = null;
		try
		{
			BasicCookieStore cookieStore = new BasicCookieStore();
			client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			HttpPost post = new HttpPost(url);
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (String temp : params.keySet())
			{
				list.add(new BasicNameValuePair(temp, params.get(temp)));
			}
			post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
			for (Map.Entry<String, String> header : headers.entrySet())
			{
				post.setHeader(header.getKey(), header.getValue());
			}
			HttpResponse response = client.execute(post);
			HttpEntity entity = response.getEntity();
			res = new Response();
			res.setCookies(cookieStore.getCookies());
			res.setStatusCode(response.getStatusLine().getStatusCode());
			res.setHeaders(response.getAllHeaders());
			res.setHttpEntity(entity);
			res.setBody(EntityUtils.toString(entity));
			return res;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if (null != client)
			{
				try
				{
					client.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

	}

	public static Header[] getDefaultHeaders()
	{
		Header[] allHeader = new BasicHeader[2];
		allHeader[0] = new BasicHeader("Content-Type", "application/x-www-form-urlencoded");
		allHeader[1] = new BasicHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		return allHeader;
	}

	private static String parseParam(Map<String, String> params)
	{
		if (null == params || params.isEmpty())
		{
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (String key : params.keySet())
		{
			sb.append(key + "=" + params.get(key) + "&");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static void main(String[] args)
	{
		String url = "http://localhost:8088/LogStat/user/auth";
		Map<String, String> params = new HashMap<String, String>();
		params.put("username", "ruanzy");
		params.put("password", "111111");
		Response res = HttpUtil.post(url, null, params);
		int state = res.getStatusCode();
		String body = res.getBody();
		System.out.println(state);
		System.out.println(body);
	}
}

class Response
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