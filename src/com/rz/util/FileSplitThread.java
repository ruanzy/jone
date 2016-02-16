package com.rz.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.CountDownLatch;

public class FileSplitThread extends Thread
{
	private String file;
	private Long start;
	private Long end;
	private String pf;
	private CountDownLatch latch;

	public FileSplitThread(String file, Long start, Long end, String pf, CountDownLatch latch)
	{
		this.file = file;
		this.start = start;
		this.end = end;
		this.pf = pf;
		this.latch = latch;
	}

	public void run()
	{
		int size = (int) (end - start + 1);
		byte[] b = new byte[size];
		OutputStream out = null;
		try
		{
			out = new FileOutputStream(new File(pf));
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(start);
			raf.readFully(b);
			out.write(b);
			out.flush();
			out.close();
			raf.close();
			System.out.println(pf + " done...");
			latch.countDown();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (out != null)
					out.close();
			}
			catch (IOException ioe)
			{
			}
		}
	}
}