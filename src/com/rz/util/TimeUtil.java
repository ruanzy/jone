package com.rz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil
{
	public static String now(String formatter)
	{
		SimpleDateFormat df = new SimpleDateFormat(formatter);
		return df.format(new Date());
	}

	public static Date parse(String time, String formatter)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat(formatter);
			return sdf.parse(time);
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}