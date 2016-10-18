package com.rz.monitor;

public class Archive
{
	String type = "AVERAGE";
	int steps;
	int rows;

	public Archive(String type, int steps, int rows)
	{
		this.type = type;
		this.steps = steps;
		this.rows = rows;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getSteps()
	{
		return steps;
	}

	public void setSteps(int steps)
	{
		this.steps = steps;
	}

	public int getRows()
	{
		return rows;
	}

	public void setRows(int rows)
	{
		this.rows = rows;
	}
}
