package com.rz.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

class UploadHelper
{
	static ServletFileUpload upload;
	Map<String, String> parameters;
	List<FileItem> items;
	static Properties prop = new Properties();
	static
	{
		InputStream is = null;
		try
		{
			upload = new ServletFileUpload(new DiskFileItemFactory());
			upload.setHeaderEncoding("UTF-8");
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream("upload.properties");
			prop.load(is);
			String maxSize = prop.getProperty("maxSize", 20*1024*1024 + "");
			upload.setSizeMax(Long.parseLong(maxSize));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public Map<String, String> getParameters()
	{
		return parameters;
	}

	public List<FileItem> getItems()
	{
		return items;
	}

	private UploadHelper()
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
	
	public static List<FileItem> getFiles()
	{
		List<FileItem> files = new ArrayList<FileItem>();
		try
		{
			List<FileItem> list = upload.parseRequest(WebUtil.Request.get());
			for (FileItem item : list)
			{
				if (!item.isFormField())
				{
					files.add(item);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return files;
	}

	public static UploadHelper parse()
	{
		UploadHelper uploader = new UploadHelper();
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
