package rzy.util;

import java.util.Set;
import rzy.core.DBMeta;

public class Test
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		Set<String> tbs = DBMeta.getTable();
		for (String string : tbs)
		{
			System.out.println(string);
		}
		DBMeta.export("users", "d:/meta");
	}

}
