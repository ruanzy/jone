package com.rz.util;

import com.rz.data.db.DBMeta;

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
