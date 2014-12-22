package service;

import java.lang.management.RuntimeMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class JvmMonitor
{

	private static Logger logger = LoggerFactory.getLogger(JvmMonitor.class);

	private long lastProcessCpuTime = 0;
	private long lastUptime = 0;

	public void record()
	{
		String message = getMemoryUsed() + " " + getCpu() + " " + getThreadCount();
		logger.info(message);
	}

	private int getThreadCount()
	{
		return ManagementFactory.getThreadMXBean().getThreadCount();
	}

	private long getMemoryUsed()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024);
	}

	private double getCpu()
	{
		OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		RuntimeMXBean runbean = java.lang.management.ManagementFactory.getRuntimeMXBean();
		long uptime = runbean.getUptime();
		long processCpuTime = osbean.getProcessCpuTime();
		int processors = osbean.getAvailableProcessors();
		double cpu = (processCpuTime - lastProcessCpuTime) / ((uptime - lastUptime) * 10000f * processors);
		lastProcessCpuTime = processCpuTime;
		lastUptime = uptime;
		return (int) cpu;
	}
}
