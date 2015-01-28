package com.rz.io;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Adler32;

public class LocalFileSync implements FileSync
{
	private int block = 512;

	public LocalFileSync()
	{

	}

	public LocalFileSync(int block)
	{
		this.block = block;
	}

	private List<Chunk> getChunks(Map<Long, Integer> checkSum, String des)
	{
		byte[] arr2 = getFileBytes(des);
		long len = arr2.length;
		int off = 0;
		List<Chunk> list = new ArrayList<Chunk>();
		while (off < len && len - off > block)
		{
			long cs = getCheckSum(arr2, off, block);
			Integer idx = checkSum.get(cs);
			if (idx != null)
			{
				list.add(new Chunk(off, idx));
				off += block;
			}
			else
			{
				off++;
			}
		}
		return list;
	}

	private List<Data> getDiff(Map<Long, Integer> checkSum, String des)
	{
		List<Chunk> chunks = getChunks(checkSum, des);
		List<Data> list = new ArrayList<Data>();
		byte[] arr = getFileBytes(des);
		if (chunks.size() > 0)
		{
			if (chunks.get(0).getOff() != 0)
			{
				int l = chunks.get(0).getOff();
				list.add(new Offset(subBytes(arr, 0, l)));
			}
			list.add(chunks.get(0));
			int size = chunks.size();
			for (int i = 1; i < size; i++)
			{
				int l = chunks.get(i).getOff() - chunks.get(i - 1).getOff() - block;
				int start = chunks.get(i - 1).getOff() + block;
				if (l != 0)
				{
					list.add(new Offset(subBytes(arr, start, l)));
				}
				list.add(chunks.get(i));
			}
			int s = chunks.get(size - 1).getOff() + block;
			int l = arr.length - s;
			list.add(new Offset(subBytes(arr, s, l)));
		}
		else
		{
			int l = arr.length;
			list.add(new Offset(subBytes(arr, 0, l)));
		}
		return list;
	}

	private Map<Long, Integer> getCheckSum(String f)
	{
		Map<Long, Integer> res = new LinkedHashMap<Long, Integer>();
		byte[] arr1 = getFileBytes(f);
		long len = arr1.length;
		int chunk = (int) (len / block);
		for (int i = 0; i < chunk; i++)
		{
			res.put(getCheckSum(arr1, i * block, block), i);
		}
		return res;
	}

	private long getCheckSum(byte[] arr, int off, int size)
	{
		Adler32 al = new Adler32();
		al.update(arr, off, size);
		return al.getValue();
	}

	public byte[] getFileBytes(String filename)
	{
		FileChannel fc = null;
		try
		{
			RandomAccessFile rf = new RandomAccessFile(filename, "r");
			fc = rf.getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0)
			{
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			rf.close();
			return result;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fc.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	private byte[] subBytes(byte[] arr, int start, int len)
	{
		byte[] newData = new byte[len];
		System.arraycopy(arr, start, newData, 0, len);
		return newData;
	}

	private byte[] getBytes(String f, int chunck)
	{
		RandomAccessFile randomFile = null;
		byte[] bytes = new byte[block];
		try
		{
			randomFile = new RandomAccessFile(f, "r");
			randomFile.seek(chunck * block);
			randomFile.read(bytes);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (randomFile != null)
			{
				try
				{
					randomFile.close();
				}
				catch (IOException e1)
				{
				}
			}
		}
		return bytes;
	}

	public void sync(String src, String des)
	{
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(des);
			Map<Long, Integer> checkSum = getCheckSum(des);
			List<Data> list = getDiff(checkSum, src);
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			for (Data data : list)
			{
				if (data instanceof Offset)
				{
					Offset o = (Offset) data;
					output.write(o.getData());
				}
				else
				{
					Chunk c = (Chunk) data;
					output.write(getBytes(des, c.getNo()));
				}
			}
			// String str = new String(output.toByteArray(), "utf-8");
			// System.out.println(str);
			output.writeTo(fos);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				fos.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}
}
