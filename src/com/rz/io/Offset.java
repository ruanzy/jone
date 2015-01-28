package com.rz.io;

public class Offset extends Data
{
	private int start;

	private int len;

	public byte[] getData()
	{
		return data;
	}

	public void setData(byte[] data)
	{
		this.data = data;
	}

	private byte[] data = new byte[len];

	public Offset(byte[] data)
	{
		this.data = data;
	}

	public int getStart()
	{
		return start;
	}

	public void setStart(int start)
	{
		this.start = start;
	}

	public int getLen()
	{
		return len;
	}

	public void setLen(int len)
	{
		this.len = len;
	}

}
