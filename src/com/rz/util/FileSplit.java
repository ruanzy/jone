package com.rz.util;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FileSplit
{
	private static List<String> getBlockPos(long total, long blocksize)
	{
		List<String> list = new ArrayList<String>();
		int block = (int) Math.ceil(total / (double) blocksize);
		try
		{
			for (int i = 1; i <= block; i++)
			{
				long b = (i - 1) * blocksize;
				long e = i * blocksize - 1;
				if (i == block)
				{
					e = total - 1;
				}
				list.add(b + "|" + e);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

	private static List<String> getLineBlockPos(String file, long blocksize)
	{
		List<String> pos = new ArrayList<String>();
		List<Long> pos1 = new ArrayList<Long>();
		pos1.add(-1L);
		try
		{
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			long total = raf.length();
			System.out.println("total " + total);
			List<String> list = getBlockPos(total, blocksize);
			//System.out.println(list);
			for (int k = 1, len = list.size(); k < len; k++)
			{
				String str = list.get(k);
				String[] a = str.split("\\|");
				Long s = Long.valueOf(a[0]);
				Long e = Long.valueOf(a[1]);
				for (Long i = s; i <= e; i++)
				{
					raf.seek(i);
					int c = raf.read();
					if (((char) c == '\r') || (char) c == '\n')
					{
						pos1.add(i);
						break;
					}
				}
			}
			raf.close();
			pos1.add(total - 1);
			//System.out.println(pos1);
			for (int k = 1, len = pos1.size(); k < len; k++)
			{
				pos.add(pos1.get(k -1) + 1 + "|" + pos1.get(k));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return pos;
	}

	public static List<String> splitFile(String file, long blocksize, String dir)
	{
		long begin = System.currentTimeMillis();
		List<String> fs = new ArrayList<String>();
		try
		{
			List<String> list = getLineBlockPos(file, blocksize);
			//System.out.println(list);
			int len = list.size();
			CountDownLatch latch = new CountDownLatch(len);
			for (int k = 0; k < len; k++)
			{
				String str = list.get(k);
				String[] a = str.split("\\|");
				Long s = Long.valueOf(a[0]);
				Long e = Long.valueOf(a[1]);
				String pf = dir + "/" + (k + 1) + ".txt";
				new FileSplitThread(file, s, e, pf, latch).start();
				fs.add(pf);
			}
			latch.await();
			long end = System.currentTimeMillis();
			System.out.println("cost " + (end - begin));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return fs;
	}

	public static void main(String[] args)
	{

		String f = "/root/gmr/customer_order.txt";
		String dir = "/root/gmr/f2";
		int blocksize = 1024*1024*200;
		try
		{
			List<String> list = splitFile(f, blocksize, dir);
			// List<String> list = fs.getLineBlockPos();
			System.out.println(list);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
