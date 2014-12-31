package org.rzy.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.rzy.web.WebUtil;

public class Uploader
{
	Map<String, String> parameters;
	List<FileItem> items;

	public Map<String, String> getParameters()
	{
		return parameters;
	}

	public List<FileItem> getItems()
	{
		return items;
	}

	private Uploader()
	{
		parameters = new HashMap<String, String>();
		items = new ArrayList<FileItem>();
	}

	public static Uploader prepare()
	{
		Uploader uploader = new Uploader();
		try
		{
			String temp = System.getProperty("java.io.tmpdir");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setRepository(new File(temp));
			factory.setSizeThreshold(1024 * 1024 * 10);
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding("utf-8");
			List<FileItem> list = upload.parseRequest(WebUtil.Request.get());
			for (FileItem item : list)
			{
				if (item.isFormField())
				{
					String name = item.getFieldName();
					String v = item.getString();
					uploader.parameters.put(name, v);
				}
				else
				{
					uploader.items.add(item);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return uploader;
	}
}
