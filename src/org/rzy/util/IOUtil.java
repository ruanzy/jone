package org.rzy.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IOUtil
{
	public static String tail(File f, long lines)
	{
		String result = null;
		long count = 0;
		if (!f.exists() || f.isDirectory() || !f.canRead())
		{
			return null;
		}
		RandomAccessFile rf = null;
		try
		{
			rf = new RandomAccessFile(f, "r");
			long length = rf.length();
			if (length == 0L)
			{
				return "";
			}
			else
			{
				long pos = length - 1;
				while (pos > 0)
				{
					pos--;
					rf.seek(pos);
					if (rf.readByte() == '\n')
					{
						count++;
						if (count == lines)
						{
							break;
						}
					}
				}
				if (pos == 0)
				{
					rf.seek(0);
				}
				byte[] bytes = new byte[(int) (length - pos)];
				rf.read(bytes);
				result = new String(bytes);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (rf != null)
			{
				try
				{
					rf.close();
				}
				catch (Exception e)
				{
				}
			}
		}
		return result.toString();
	}
}
