package com.rz.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.beanutils.BeanUtils;

public class FileItem
{
	int id;
	String name;
	int pid = -1;
	String path;
	long size;
	String createtime;
	boolean isdir;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getPid()
	{
		return pid;
	}

	public void setPid(int pid)
	{
		this.pid = pid;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public long getSize()
	{
		return size;
	}

	public void setSize(long size)
	{
		this.size = size;
	}

	public String getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(String createtime)
	{
		this.createtime = createtime;
	}

	public boolean isIsdir()
	{
		return isdir;
	}

	public void setIsdir(boolean isdir)
	{
		this.isdir = isdir;
	}

	public Map<String, Object> toMap()
	{
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			BeanUtils.populate(this, map);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	public static void main(String[] args)
	{
		String dir = "d:/upload";
		File f = new File(dir, "a.txt");
		FileItem fi = new FileItem();
		fi.setId(1);
		fi.setName(f.getName());
		fi.setSize(f.length());
		fi.setPid(-1);
		fi.setPath("1");
		Map<String, Object> m = transBean2Map(fi);
		System.out.println(m);
	}

	public static Map<String, Object> transBean2Map(Object obj)
	{

		if (obj == null)
		{
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors)
			{
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class"))
				{
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					map.put(key, value);
				}

			}
		}
		catch (Exception e)
		{
			System.out.println("transBean2Map Error " + e);
		}

		return map;

	}
}
