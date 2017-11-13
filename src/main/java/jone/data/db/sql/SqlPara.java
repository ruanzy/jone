package jone.data.db.sql;

import java.util.ArrayList;
import java.util.List;

public class SqlPara {
	
	String sql;
	List<Object> paraList;
	
	public SqlPara setSql(String sql) {
		this.sql = sql;
		return this;
	}
	
	public SqlPara addPara(Object para) {
		if (paraList == null) {
			paraList = new ArrayList<Object>();
		}
		paraList.add(para);
		return this;
	}
	
	public String getSql() {
		return sql;
	}
	
	public Object[] getPara() {
		if (paraList == null || paraList.size() == 0) {
			return new Object[0];
		} else {
			return paraList.toArray(new Object[paraList.size()]);
		}
	}
	
	public SqlPara clear() {
		sql = null;
		if (paraList != null) {
			paraList.clear();
		}
		return this;
	}
	
	public String toString() {
		return "Preparing: " + sql + "\nParameters: " + paraList;
	}
}
