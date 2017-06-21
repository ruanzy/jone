package com.rz.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Uploader
{
	static ServletFileUpload upload;
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

	public static boolean isMultipart()
	{
		return ServletFileUpload.isMultipartContent(WebUtil.Request.get());
	}

	public static void init(long maxSize)
	{
		upload = new ServletFileUpload(new DiskFileItemFactory());
		upload.setHeaderEncoding("UTF-8");
		if (maxSize > 0)
		{
			upload.setSizeMax(maxSize);
		}
	}

	public static Uploader parse()
	{
		Uploader uploader = new Uploader();
		try
		{
			List<FileItem> list = upload.parseRequest(WebUtil.Request.get());
			for (FileItem item : list)
			{
				if (item.isFormField())
				{
					String name = item.getFieldName();
					String v = item.getString("UTF-8");
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

	public Map<String, String> upload(String upDir)
	{
		Map<String, String> files = new HashMap<String, String>();
		for (FileItem item : this.items)
		{
			String srcName = item.getName();
			File savedFile = new File(upDir, srcName);
			try
			{
				item.write(savedFile);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			files.put(item.getFieldName(), savedFile.getPath());
		}
		return files;
	}
}
