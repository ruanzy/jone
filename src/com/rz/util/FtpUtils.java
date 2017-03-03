package com.rz.util;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPFile;
import java.io.File;

public class FtpUtils
{
	private static ThreadLocal<FTPClient> tfc = new ThreadLocal<FTPClient>();

	public static void connect(String host, int port, String username, String password)
	{
		FTPClient client = new FTPClient();
		try
		{
			boolean compressionSupported = client.isCompressionSupported();
			client.setCompressionEnabled(compressionSupported);
			client.setCharset("utf-8");
			client.setType(FTPClient.TYPE_BINARY);
			client.setPassive(true);
			client.connect(host, port);
			client.login(username, password);
			tfc.set(client);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 切换目录
	 * 
	 * @param dir
	 *            目录名
	 */
	public static void changeDirectory(String dir)
	{
		FTPClient client = tfc.get();
		try
		{
			client.changeDirectory(dir);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param dir
	 *            目录名
	 */
	public static void createDirectory(String dir)
	{
		FTPClient client = tfc.get();
		try
		{
			client.createDirectory(dir);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 文件重命名
	 * 
	 * @param oldname
	 *            旧文件名
	 * @param newname
	 *            新文件名
	 */
	public static void rename(String oldname, String newname)
	{
		FTPClient client = tfc.get();
		try
		{
			client.rename(oldname, newname);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定文件
	 * 
	 * @param file
	 *            文件路径
	 */
	public static void deleteFile(String file)
	{
		FTPClient client = tfc.get();
		try
		{
			client.deleteFile(file);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 删除指定目录(注意该目录必须为空)
	 * 
	 * @param dir
	 *            目录名
	 */
	public static void deleteDirectory(String dir) throws Exception
	{
		FTPClient client = tfc.get();
		try
		{
			client.deleteDirectory(dir);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 获得文件size
	 * 
	 * @param file
	 *            文件名
	 */
	public static long fileSize(String file)
	{
		FTPClient client = tfc.get();
		long size = 0;
		try
		{
			size = client.fileSize(file);
		}
		catch (Exception e)
		{

		}
		return size;
	}

	/**
	 * 列出指定目录下的文件名
	 * 
	 * @param dir
	 *            目录名
	 */
	public static String[] listNames(String dir)
	{
		FTPClient client = tfc.get();
		try
		{
			client.changeDirectory(dir);
			return client.listNames();
		}
		catch (Exception e)
		{

		}
		return new String[0];
	}

	/**
	 * 列出指定目录下的文件
	 * 
	 * @param dir
	 *            目录名
	 */
	public static FTPFile[] listFile(String dir)
	{
		FTPClient client = tfc.get();
		try
		{
			client.changeDirectory(dir);
			return client.list();
		}
		catch (Exception e)
		{

		}
		return new FTPFile[0];
	}

	/**
	 * 列出当前目录下复合通配符的文件
	 * 
	 * @param wildcard
	 *            通配符 如:*.txt
	 */
	public static FTPFile[] listWildcard(String wildcard) throws Exception
	{
		FTPClient client = tfc.get();
		try
		{
			return client.list(wildcard);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	/**
	 * 下载文件到本地目录
	 * 
	 * @param remoteFile
	 *            文件路径
	 * @param localFolder
	 *            本地目录
	 */
	public static void download(String remoteFile, File localFolder)
	{
		FTPClient client = tfc.get();
		if (localFolder.isFile())
		{
			throw new RuntimeException("所要的下载保存的地方是一个文件，无法保存！");
		}
		else
		{
			if (!localFolder.exists())
				localFolder.mkdirs();
		}
		try
		{
			String fileName = new File(remoteFile).getName();
			File localFile = new File(localFolder, fileName);
			client.download(remoteFile, localFile);
			client.changeDirectory("/");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * 上传本地文件到指定目录下
	 * 
	 * @param localFile
	 *            本地文件
	 * @param remoteFolder
	 *            上传目录
	 */
	public static void upload(String localFile, String remoteFolder)
	{
		try
		{
			FTPClient client = tfc.get();
			client.changeDirectory(remoteFolder);
			File localfile = new File(localFile);
			client.upload(localfile);
			client.changeDirectory("/");
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void close()
	{
		FTPClient client = tfc.get();
		if (client != null)
		{
			try
			{
				client.logout();
			}
			catch (Exception e)
			{

			}
			finally
			{
				if (client.isConnected())
				{
					try
					{
						client.disconnect(true);
						tfc.remove();
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}
}