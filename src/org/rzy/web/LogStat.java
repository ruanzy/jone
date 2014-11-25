package org.rzy.web;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogStat
{
	private final String webRoot;

	public LogStat(String webRoot)
	{
		this.webRoot = webRoot;
	}

	static ScheduledExecutorService pool = Executors.newSingleThreadScheduledExecutor();

	public void start()
	{
		pool.scheduleAtFixedRate(new Runnable()
		{

			public void run()
			{
				try
				{
					String logpath = webRoot + "log/log.txt";
					String record = webRoot + "log/record.txt";
					FileReader fr = new FileReader(record);
					BufferedReader br = new BufferedReader(fr);
					String l = br.readLine();
					fr.close();
					long len1 = Long.valueOf(l);
					RandomAccessFile rf2 = new RandomAccessFile(logpath, "rw");
					long len2 = rf2.length();
					if (len2 > len1)
					{
						rf2.seek(len1);
						String str = rf2.readLine();
						System.out.println(new String(str.getBytes("ISO8859-1"), "UTF-8"));
						FileWriter fw = new FileWriter(record);
						fw.write(String.valueOf(len2));
						fw.close();
					}
					rf2.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		}, 0, 1, TimeUnit.SECONDS);
	}
}
