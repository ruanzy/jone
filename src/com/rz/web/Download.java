package com.rz.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;

public class Download
{

	public static void down(String dir, String file)
	{
		WebUtil.Response.get().setHeader("Content-type", "text/plain;charset=UTF-8");
		WebUtil.Response.get().setHeader("Content-Disposition", "attachment; filename=" + file);
		WebUtil.Response.get().setCharacterEncoding("UTF-8");
		File f = new File(dir, file);
		OutputStream pw = null;
		try
		{
			pw = WebUtil.Response.get().getOutputStream();
			IOUtils.copy(new FileInputStream(f), pw);
		}
		catch (Exception e)
		{

		}
		finally
		{
			try
			{
				pw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
