package com.rz.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

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

	public static Uploader prepare(long maxSize) throws Exception
	{
		boolean isMultipart = ServletFileUpload.isMultipartContent(WebUtil.Request.get());
		if (!isMultipart)
		{
			return null;
		}
		Uploader uploader = new Uploader();
		String temp = System.getProperty("java.io.tmpdir");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(temp));
		factory.setSizeThreshold(1024 * 1024 * 10);
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("utf-8");
		if (maxSize > 0)
		{
			upload.setSizeMax(maxSize);
		}
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
		return uploader;
	}

	public Map<String, String> upload(String upDir) throws Exception
	{
		Map<String, String> files = new HashMap<String, String>();
		for (FileItem item : this.items)
		{
			String srcName = item.getName();
			String randomName = UUID.randomUUID().toString() + srcName.substring(srcName.lastIndexOf("."));
			File savedFile = new File(upDir, randomName);
			item.write(savedFile);
			files.put(item.getFieldName(), randomName);
		}
		return files;
	}
}
