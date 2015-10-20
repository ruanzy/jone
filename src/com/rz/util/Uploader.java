package com.rz.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
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

	public static Uploader prepare(long maxSize)
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
		List<FileItem> list = null;
		try
		{
			list = upload.parseRequest(WebUtil.Request.get());
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
		catch (FileUploadException e)
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
			int idx = srcName.lastIndexOf("/");
			StringBuffer newName = new StringBuffer();
			newName.append(UUID.randomUUID().toString() + "_");
			if(idx > 0){
				newName.append(srcName.substring(idx));
			}else{
				newName.append(srcName);
			}
			File savedFile = new File(upDir, newName.toString());
			try
			{
				item.write(savedFile);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			files.put(item.getFieldName(), newName.toString());
		}
		return files;
	}
}
