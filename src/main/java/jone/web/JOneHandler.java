package jone.web;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class JOneHandler extends AbstractHandler
{

	@Override
	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException
	{
		if ("/".equals(target)){
			response.setCharacterEncoding("utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("jone/resource/index.html");
			try
			{
				IOUtils.copy(in, response.getOutputStream());
			}
			finally
			{
				IOUtils.closeQuietly(in);
			}
			return;
		}
		boolean isStatic = (target.lastIndexOf(".") != -1);
		if (isStatic)
		{
			response.setCharacterEncoding("utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			baseRequest.setHandled(true);
			InputStream in = this.getClass().getClassLoader().getResourceAsStream("jone/resource" + target);
			try
			{
				IOUtils.copy(in, response.getOutputStream());
			}
			finally
			{
				IOUtils.closeQuietly(in);
			}
		}
		else
		{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
