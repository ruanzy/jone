package com.rz.util;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import com.sun.management.OperatingSystemMXBean;

public class JmxUtil
{
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static int getPid(String ip, int jmxport) throws Exception
	{
		JMXConnector jmxConnector = null;
		String jmxURL = "service:jmx:rmi:///jndi/rmi://" + ip + ":" + jmxport + "/jmxrmi";
		JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);
		jmxConnector = JMXConnectorFactory.connect(serviceURL);
		MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
		RuntimeMXBean runtimeMXBean = ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
				ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
		return Integer.parseInt(runtimeMXBean.getName().split("@")[0]);
	}

	public Map<String, Object> getJvmInfo(String ip, String jmxport)
	{
		JMXConnector jmxConnector = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try
		{
			String jmxURL = "service:jmx:rmi:///jndi/rmi://" + ip + ":" + jmxport + "/jmxrmi";
			JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);
			jmxConnector = JMXConnectorFactory.connect(serviceURL);
			MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
			// Java 虚拟机的运行时系统
			RuntimeMXBean runtimeMXBean = ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
					ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
			map.put("specName", runtimeMXBean.getSpecName());
			map.put("specVendor", runtimeMXBean.getSpecVendor());
			map.put("specVersion", runtimeMXBean.getSpecVersion());
			map.put("startTime", df.format(runtimeMXBean.getStartTime()));
			map.put("vmName", runtimeMXBean.getVmName());
			map.put("vmVendor", runtimeMXBean.getVmVendor());
			map.put("vmVersion", runtimeMXBean.getVmVersion());
			// Java 虚拟机内存系统
			MemoryMXBean memoryMXBean = ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
					ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
			MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
			MemoryUsage nonHeapMemoryUsage = memoryMXBean.getNonHeapMemoryUsage();
			map.put("hmu_init", convertToGigabyte(heapMemoryUsage.getInit()));
			map.put("hmu_used", convertToGigabyte(heapMemoryUsage.getUsed()));
			map.put("hmu_committed", convertToGigabyte(heapMemoryUsage.getCommitted()));
			map.put("hmu_max", convertToGigabyte(heapMemoryUsage.getMax()));
			map.put("nhmu_init", convertToGigabyte(nonHeapMemoryUsage.getInit()));
			map.put("nhmu_used", convertToGigabyte(nonHeapMemoryUsage.getUsed()));
			map.put("nhmu_committed", convertToGigabyte(nonHeapMemoryUsage.getCommitted()));
			map.put("nhmu_max", convertToGigabyte(nonHeapMemoryUsage.getMax()));
			double mem_usage = heapMemoryUsage.getUsed() * 1.0 / heapMemoryUsage.getCommitted() * 100;
			map.put("mem_usage", mem_usage);

			ThreadMXBean threadMXBean = ManagementFactory.newPlatformMXBeanProxy(mBeanServerConnection,
					ManagementFactory.THREAD_MXBEAN_NAME, ThreadMXBean.class);
			double thread_daemon = threadMXBean.getDaemonThreadCount();
			double thread_started = threadMXBean.getTotalStartedThreadCount();
			double thread_active = threadMXBean.getThreadCount();
			map.put("thread_daemon", thread_daemon);
			map.put("thread_started", thread_started);
			map.put("thread_active", thread_active);

			com.sun.management.OperatingSystemMXBean opMXbean = ManagementFactory.newPlatformMXBeanProxy(
					mBeanServerConnection, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME,
					com.sun.management.OperatingSystemMXBean.class);
			map.put("arch", opMXbean.getArch());
			map.put("availableProcessors", opMXbean.getAvailableProcessors());
			map.put("name", opMXbean.getName());
			map.put("systemLoadAverage", opMXbean.getSystemLoadAverage());
			map.put("version", opMXbean.getVersion());
			Long start = System.currentTimeMillis();
			long startT = opMXbean.getProcessCpuTime();
			try
			{
				TimeUnit.SECONDS.sleep(2);
			}
			catch (InterruptedException e)
			{

			}
			Long end = System.currentTimeMillis();
			long endT = opMXbean.getProcessCpuTime();
			double cpu_usage = (endT - startT) / 10000.0 / (end - start) / opMXbean.getAvailableProcessors();
			map.put("cpu_usage", cpu_usage);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				jmxConnector.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return map;
	}

	public static double convertToGigabyte(long memory)
	{
		return ((int) (memory / 1024.0 / 1024 / 1024 * 100)) / 100.0;
	}

	public static void syd(String ip, String jmxport)
	{
		JMXConnector jmxConnector = null;
		String jmxURL = "service:jmx:rmi:///jndi/rmi://" + ip + ":" + jmxport + "/jmxrmi";
		try
		{
			JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);
			jmxConnector = JMXConnectorFactory.connect(serviceURL);
			MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();
			com.sun.management.OperatingSystemMXBean opMXbean = ManagementFactory.newPlatformMXBeanProxy(
					mBeanServerConnection, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME,
					com.sun.management.OperatingSystemMXBean.class);
			long t = opMXbean.getTotalPhysicalMemorySize() / 1024 / 1024 / 1024;
			System.out.println(t);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				jmxConnector.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void syd2(String ip, String jmxport)
	{
		try
		{
			OperatingSystemMXBean opMXbean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
					.getOperatingSystemMXBean();
			long t = opMXbean.getTotalPhysicalMemorySize() / 1024 / 1024;
			long f = opMXbean.getFreePhysicalMemorySize() / 1024 / 1024;
			System.out.println(t);
			System.out.println(f);
			System.out.println((t - f) * 1.0 / 1024);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		String ip = "11.0.1.83";
		String jmxport = "9401";
		syd2(ip, jmxport);
	}
}
