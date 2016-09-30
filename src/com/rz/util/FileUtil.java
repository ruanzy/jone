package com.rz.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public class FileUtil
{

	public static List<String> splitByRow(String file, String dir, int rows)
	{
		List<String> list = new ArrayList<String>();
		File sourceFile = new File(file);
		File targetFile = new File(dir);
		if (!sourceFile.exists() || rows <= 0 || sourceFile.isDirectory())
		{
			return null;
		}
		if (targetFile.exists())
		{
			if (!targetFile.isDirectory())
			{
				return null;
			}
		}
		else
		{
			targetFile.mkdirs();
		}
		try
		{
			String fn = FilenameUtils.getName(sourceFile.getName());
			String ext = FilenameUtils.getExtension(sourceFile.getName());
			InputStreamReader in = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader br = new BufferedReader(in);

			BufferedWriter bw = null;
			String str = "";
			String tempData = br.readLine();
			int i = 1, s = 0;
			while (tempData != null)
			{
				str += tempData + "\r\n";
				if (i % rows == 0)
				{
					String pname = targetFile.getAbsolutePath() + File.separator + fn + "_" + (s + 1) + "." + ext;
					bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pname), "UTF-8"), 1024);

					bw.write(str);
					bw.close();
					list.add(pname);
					str = "";
					s += 1;
				}
				i++;
				tempData = br.readLine();
			}
			if ((i - 1) % rows != 0)
			{
				String pname = targetFile.getAbsolutePath() + File.separator + fn + "_" + (s + 1) + "." + ext;
				bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pname), "UTF-8"), 1024);
				bw.write(str);
				bw.close();
				br.close();
				list.add(pname);
				s += 1;
			}
			in.close();
		}
		catch (Exception e)
		{
		}
		return list;
	}

	@SuppressWarnings("resource")
	public static void mergeFiles(List<String> files, String outFile)
	{
		int BUFSIZE = 1024 * 8;
		FileChannel outChannel = null;
		try
		{
			outChannel = new FileOutputStream(outFile).getChannel();
			for (String f : files)
			{
				FileChannel fc = new FileInputStream(f).getChannel();
				ByteBuffer bb = ByteBuffer.allocate(BUFSIZE);
				while (fc.read(bb) != -1)
				{
					bb.flip();
					outChannel.write(bb);
					bb.clear();
				}
				fc.close();
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			try
			{
				if (outChannel != null)
				{
					outChannel.close();
				}
			}
			catch (IOException ignore)
			{
			}
		}
	}

	public static int getLineNum(File file)
	{
		InputStream is = null;
		int lineNum = 0;
		try
		{
			is = new BufferedInputStream(new FileInputStream(file));
			byte[] c = new byte[2048];
			int readChars = 0;
			while ((readChars = is.read(c)) != -1)
			{
				for (int i = 0; i < readChars; ++i)
				{
					if (c[i] == '\n')
					{
						++lineNum;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return lineNum;
	}
}