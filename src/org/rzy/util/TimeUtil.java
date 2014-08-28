package org.rzy.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil
{
	public static String now(String formatter)
	{
		SimpleDateFormat df = new SimpleDateFormat(formatter);
		return df.format(new Date());
	}
}