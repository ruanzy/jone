package rzy.util;

import rzy.core.Dao;

public class ExportTask implements Runnable
{

	String table;

	public ExportTask(String table)
	{
		this.table = table;
	}

	public void run()
	{
		Dao.export(table);
		System.out.println(table + " done!");
	}

}
