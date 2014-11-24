package org.rzy.web;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.TimerTask;

public class LogStat extends TimerTask
{

	private String webRoot;

	public LogStat(String webRoot)
	{
		this.webRoot = webRoot;
	}

	@Override
	public void run()
	{
		try
		{
			String logpath = webRoot + "log/log.txt";
			String record = webRoot + "log/record.txt";
			RandomAccessFile rf1 = new RandomAccessFile(record, "rw");
			RandomAccessFile rf2 = new RandomAccessFile(logpath, "rw");
			int len1 = Integer.getInteger(rf1.readLine());
			int len2 = (int)rf2.length();
			byte[] buff = new byte[len2-len1];  
			if(len2>len1){
				rf2.seek(len1);
				rf2.read(buff, len1, len2-len1);
			}
			System.out.println(buff.toString());
			rf1.write(len2);
			rf1.close();
			rf2.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
