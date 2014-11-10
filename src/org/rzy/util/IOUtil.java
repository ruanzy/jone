package org.rzy.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IOUtil
{
	public static String tail(String file, long lines)
	{
		// 定义结果集
		StringBuffer result = new StringBuffer();
		// 行数统计
		long count = 0;
		
		File f = new File(file);

		// 排除不可读状态
		if (!f.exists() || f.isDirectory() || !f.canRead())
		{
			return null;
		}

		// 使用随机读取
		RandomAccessFile fileRead = null;
		try
		{
			// 使用读模式
			fileRead = new RandomAccessFile(f, "r");
			// 读取文件长度
			long length = fileRead.length();
			// 如果是0，代表是空文件，直接返回空结果
			if (length == 0L)
			{
				return "";
			}
			else
			{
				// 初始化游标
				long pos = length - 1;
				while (pos > 0)
				{
					pos--;
					// 开始读取
					fileRead.seek(pos);
					// 如果读取到\n代表是读取到一行
					if (fileRead.readByte() == '\n')
					{
						// 使用readLine获取当前行
						String line = fileRead.readLine();
						// 保存结果
						result.append(line);
						
						result.append("\r\n");

						// 行数统计，如果到达了numRead指定的行数，就跳出循环
						count++;
						if (count == lines)
						{
							break;
						}
					}
				}
				if (pos == 0)
				{
					fileRead.seek(0);
					result.append(fileRead.readLine());
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fileRead != null)
			{
				try
				{
					fileRead.close();
				}
				catch (Exception e)
				{
				}
			}
		}
		return result.toString();
	}
}
