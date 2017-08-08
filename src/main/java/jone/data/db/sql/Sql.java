package jone.data.db.sql;

import java.util.List;

public class Sql {
	protected String sql;
	protected List<Object> params;

	public String getSql() {
		return sql;
	}

	public List<Object> getParams() {
		return params;
	}
}