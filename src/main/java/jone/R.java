package jone;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Clob;
import java.util.HashMap;

public class R extends HashMap<String, Object>
{
	private static final long serialVersionUID = 4112578634029874840L;

	public String getString(String columnName)
	{
		Object o = this.get(columnName);
		return (o == null) ? null : String.valueOf(this.get(columnName));
	}

	public Integer getInt(String columnName)
	{
		return Integer.valueOf(this.get(columnName).toString());
	}
	
	public Double getDouble(String columnName)
	{
		return Double.valueOf(this.get(columnName).toString());
	}

	public Long getLong(String columnName)
	{
		return Long.valueOf(this.get(columnName).toString());
	}

	public String getClob(String columnName)
	{
		Object o = this.get(columnName);
		if (o == null)
		{
			return null;
		}
		if (o instanceof String)
		{
			return o.toString();
		}
		StringBuffer sb = new StringBuffer();
		Reader is = null;
		try
		{
			is = ((Clob) o).getCharacterStream();
			BufferedReader br = new BufferedReader(is);
			String s = br.readLine();
			while (s != null)
			{
				sb.append(s);
				s = br.readLine();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}
}
