package org.rzy.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LogListener implements ServletContextListener
{

	public void contextDestroyed(ServletContextEvent arg0)
	{
		System.getProperties().remove("logDir");
	}

	public void contextInitialized(ServletContextEvent arg0)
	{
		final String webRoot = arg0.getServletContext().getRealPath("/");
		final String logDir = webRoot + "log";
		System.setProperty("logDir", logDir);
		// ScheduledExecutorService pool =
		// Executors.newSingleThreadScheduledExecutor();
		// pool.scheduleAtFixedRate(new Runnable()
		// {
		// public void run()
		// {
		// try
		// {
		// String logpath = logDir + "log/log.txt";
		// String record = logDir + "log/record.txt";
		// FileReader fr = new FileReader(record);
		// BufferedReader br = new BufferedReader(fr);
		// String l = br.readLine();
		// fr.close();
		// long len1 = Long.valueOf(l);
		// RandomAccessFile rf2 = new RandomAccessFile(logpath, "rw");
		// long len2 = rf2.length();
		// if (len2 > len1)
		// {
		// rf2.seek(len1);
		// String str = rf2.readLine();
		// System.out.println(new String(str.getBytes("ISO8859-1"), "UTF-8"));
		// System.out.println(new String(str.getBytes("ISO8859-1"), "UTF-8"));
		// FileWriter fw = new FileWriter(record);
		// fw.write(String.valueOf(len2));
		// fw.close();
		// }
		// rf2.close();
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// }
		//
		// }
		// }, 0, 1, TimeUnit.SECONDS);
	}
}
