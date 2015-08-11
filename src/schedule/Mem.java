package schedule;

import java.lang.management.RuntimeMXBean;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import sun.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class Mem implements Job
{

	private long lastProcessCpuTime = 0;
	private long lastUptime = 0;

	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		String message = getMemoryUsed() + " " + getCpu() + " " + getThreadCount();
		System.out.println(message);
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
