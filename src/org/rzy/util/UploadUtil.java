package org.rzy.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.rzy.web.WebUtil;

public class UploadUtil
{
	public static void upload(String path)
	{
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(path));
		factory.setSizeThreshold(1024 * 1024 * 10);
		ServletFileUpload upload1 = new ServletFileUpload(factory);
		upload1.setHeaderEncoding("utf-8");
		try
		{
			List<FileItem> list = upload1.parseRequest(WebUtil.getRequest());
			for (FileItem item : list)
			{
				if (item.isFormField())
				{
					String name = item.getFieldName();
					String value = item.getName();
					WebUtil.getRequest().setAttribute(name, value);
				}
				else
				{
					String value = item.getName();
					value = value.substring(value.lastIndexOf("\\") + 1, value.length());
					InputStream input = item.getInputStream();
					OutputStream output = new FileOutputStream(new File(path, value));
					IOUtils.copy(input, output);
					IOUtils.closeQuietly(input);
					IOUtils.closeQuietly(output);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
