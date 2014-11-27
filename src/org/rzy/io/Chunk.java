package org.rzy.io;

public class Chunk extends Data
{
	private int off;

	private int No;

	public Chunk(int off, int no)
	{
		this.off = off;
		this.No = no;
	}

	public int getOff()
	{
		return off;
	}

	public void setOff(int off)
	{
		this.off = off;
	}

	public int getNo()
	{
		return No;
	}

	public void setNo(int no)
	{
		No = no;
	}

}
