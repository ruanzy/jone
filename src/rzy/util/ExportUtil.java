package rzy.util;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import rzy.core.Dao;

public class ExportUtil
{
	static ExecutorService pool = Executors.newCachedThreadPool();

	public static void export()
	{
		Set<String> tables = Dao.getTable();
		for (String string : tables)
		{
			pool.execute(new ExportTask(string));
		}
	}
}
