package rzy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util
{
	static MessageDigest md = null;

	static
	{
		try
		{
			md = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException ne)
		{
			ne.printStackTrace();
		}
	}

	public static String md5(File f)
	{
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(f);
			byte[] buffer = new byte[8192];
			int length;
			while ((length = fis.read(buffer)) != -1)
			{
				md.update(buffer, 0, length);
			}

			return new String(Hex.encodeHex(md.digest()));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try
			{
				if (fis != null)
					fis.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static String md5(String target)
	{
		return DigestUtils.md5Hex(target);
	}
}