package com.rz.util;

import java.util.concurrent.CountDownLatch;

public class ImportThread extends Thread
{
	String file;
	CountDownLatch latch;

	public ImportThread(String file, CountDownLatch latch)
	{
		this.file = file;
		this.latch = latch;
	}

	public void run()
	{
		doWork();
		latch.countDown();
	}

	private void doWork()
	{
		try
		{
			System.out.println("Importing " + this.file + "...");
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}