package org.rzy.util;

import java.util.ArrayList;
import java.util.List;

public class Where
{
	private List<FOV> condList;

	private String orderBy = "";

	private Where()
	{
		this.condList = new ArrayList<FOV>();
	}

	public static Where create()
	{
		return new Where();
	}

	public Where add(String field, String op, Object... values)
	{
		this.condList.add(new FOV(field, op, values));
		return this;
	}

	public Where orderby(String fs)
	{
		this.orderBy = " ORDER BY " + fs;
		return this;
	}

	private StringBuffer where()
	{
		StringBuffer where = new StringBuffer();
		for (FOV fov : condList)
		{
			String field = fov.getField();
			String op = fov.getOp();
			Object[] values = fov.getValues();
			if ("=".equals(op) || ">".equals(op) || ">=".equals(op) || "<".equals(op) || "<=".equals(op)
					|| "<>".equals(op))
			{
				if (where.length() != 0)
				{
					where.append(" AND ");
				}
				where.append(field).append(op);
				if (values[0] instanceof String)
				{
					where.append("\'");
				}
				where.append(values[0]);
				if (values[0] instanceof String)
				{
					where.append("\'");
				}
			}
			else if ("LIKE".equalsIgnoreCase(op))
			{
				if (where.length() != 0)
				{
					where.append(" AND ");
				}
				where.append(field).append(" LIKE \'%").append(values[0]).append("%\'");
			}
			else if ("BETWEEN".equalsIgnoreCase(op))
			{
				if (where.length() != 0)
				{
					where.append(" AND ");
				}
				where.append("(").append(field).append(" BETWEEN ");
				if (values[0] instanceof String)
				{
					where.append("\'");
				}
				where.append(values[0]);
				if (values[0] instanceof String)
				{
					where.append("\'");
				}
				where.append(" AND ");
				if (values[1] instanceof String)
				{
					where.append("\'");
				}
				where.append(values[1]);
				if (values[1] instanceof String)
				{
					where.append("\'");
				}
			}
			else if ("IN".equalsIgnoreCase(op))
			{
				if (where.length() != 0)
				{
					where.append(" AND ");
				}
				where.append(field).append(" IN(").append(values[0]).append(")");
			}
		}
		return where;
	}

	public String appendTo(String baseSQL)
	{
		StringBuffer sql = new StringBuffer(baseSQL);
		StringBuffer where = where();
		if (where.length() > 0)
		{
			sql.append(" WHERE ").append(where());
		}
		if (this.orderBy.length() > 0)
		{
			sql.append(this.orderBy);
		}
		return sql.toString();
	}
}

class FOV
{
	public String getField()
	{
		return field;
	}

	public String getOp()
	{
		return op;
	}

	public Object[] getValues()
	{
		return values;
	}

	private String field;
	private String op;
	private Object[] values;

	public FOV(String field, String op, Object... values)
	{
		this.field = field;
		this.op = op;
		this.values = values;
	}
}
