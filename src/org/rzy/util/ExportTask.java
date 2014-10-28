package org.rzy.util;

import org.rzy.dao.DBMeta;

public class ExportTask implements Runnable
{

	String table;

	public ExportTask(String table)
	{
		this.table = table;
	}

	public void run()
	{
		DBMeta.export(table, "d:/meta");
		System.out.println(table + " done!");
	}

}
