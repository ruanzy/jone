package com.rz.common;

import java.util.HashMap;

public class R extends HashMap<String, Object>
{
	private static final long serialVersionUID = 4112578634029874840L;

	public String getString(String columnName)
	{
		return String.valueOf(this.get(columnName));
	}

	public Integer getInt(String columnName)
	{
		return Integer.valueOf(this.get(columnName).toString());
	}

	public Long getLong(String columnName)
	{
		return Long.valueOf(this.get(columnName).toString());
	}
}
