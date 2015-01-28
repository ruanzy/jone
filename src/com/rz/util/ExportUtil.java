package com.rz.util;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.rz.dao.DBMeta;

public class ExportUtil
{
	static ExecutorService pool = Executors.newCachedThreadPool();

	public static void export()
	{
		Set<String> tables = DBMeta.getTable();
		for (String string : tables)
		{
			pool.execute(new ExportTask(string));
		}
	}
}
