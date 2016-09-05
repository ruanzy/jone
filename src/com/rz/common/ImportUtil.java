package com.rz.common;

import java.io.File;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ImportUtil
{

	public static void importFile(String file)
	{
		try
		{
			File sourceFile = new File(file);
			String d = sourceFile.getParentFile().getPath();
			List<String> list = FileUtil.splitByRow(file, d, 3);
			int size = list.size();
			CountDownLatch latch = new CountDownLatch(size);
			for (String f : list)
			{
				new ImportThread(f, latch).start();
			}
			latch.await();
			System.out.println("all work done");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
