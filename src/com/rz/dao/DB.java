package com.rz.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.common.R;

public final class DB
{
	private DataSource ds = null;
	private ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
	private ThreadLocal<Statement> sl = new ThreadLocal<Statement>();
	private ThreadLocal<Boolean> begintx = new ThreadLocal<Boolean>();
	boolean showsql = false;
	Logger log = LoggerFactory.getLogger(DB.class);

	private static class SingletonHolder
	{
		private final static DB INSTANCE = new DB();
	}

	public static DB getInstance()
	{
		SingletonHolder.INSTANCE.begintx.set(false);
		return SingletonHolder.INSTANCE;
	}

	public DB()
	{
		InputStream is = null;
		Properties prop = new Properties();
		try
		{
			String fileName = "dao.properties";
			is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (is == null)
			{
				throw new IllegalArgumentException("properties file not found in classpath: " + fileName);
			}
			prop = new Properties();
			prop.load(new InputStreamReader(is, "UTF-8"));
			ds = BasicDataSourceFactory.createDataSource(prop);
			begintx.set(false);
		}
		catch (Exception e)
		{
			log.debug("Create DataSource Exception!");
			throw new DataAccessException("Create DataSource Exception!", e);
		}
	}

	public DB(DataSource ds)
	{
		this.ds = ds;
	}

	public synchronized void showSql(boolean isShowsql)
	{
		showsql = isShowsql;
	}

	private synchronized Connection getConnection()
	{
		Connection conn = null;
		try
		{
			if (ds != null)
			{
				conn = ds.getConnection();
			}
		}
		catch (Exception e)
		{
			throw new DataAccessException(e.getMessage());
		}
		return conn;
	}

	public static void release(ResultSet rs, Statement st, Connection conn)
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			rs = null;
		}
		if (st != null)
		{
			try
			{
				st.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			st = null;
		}
		if (conn != null)
		{
			try
			{
				conn.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public int update(String sql, Object... params)
	{
		int result = 0;
		Boolean flag = begintx.get();
		if (flag != null && flag.booleanValue())
		{
			try
			{
				Connection conn = tl.get();
				PreparedStatement ps = conn.prepareStatement(sql);
				setParams(ps, params);
				showSQL(sql, params);
				result = ps.executeUpdate();
			}
			catch (SQLException e)
			{
				throw new DataAccessException(e.getMessage());
			}
		}
		else
		{
			Connection conn = null;
			PreparedStatement ps = null;
			try
			{
				conn = getConnection();
				ps = conn.prepareStatement(sql);
				setParams(ps, params);
				showSQL(sql, params);
				result = ps.executeUpdate();
			}
			catch (SQLException e)
			{
				throw new DataAccessException(e.getMessage());
			}
			finally
			{
				release(null, ps, conn);
			}
		}
		return result;
	}

	public void insertBatch(String sql, Object[][] params)
	{
		Boolean flag = begintx.get();
		if (flag != null && flag.booleanValue())
		{
			try
			{
				Connection conn = tl.get();
				PreparedStatement ps = conn.prepareStatement(sql);
				for (int i = 0; i < params.length; i++)
				{
					setParams(ps, params[i]);
					ps.addBatch();
				}
				ps.executeBatch();
			}
			catch (SQLException e)
			{
				throw new DataAccessException(e.getMessage());
			}
		}
		else
		{
			Connection conn = null;
			PreparedStatement ps = null;
			try
			{
				conn = getConnection();
				ps = conn.prepareStatement(sql);
				for (int i = 0; i < params.length; i++)
				{
					setParams(ps, params[i]);
					ps.addBatch();
				}
				ps.executeBatch();
			}
			catch (SQLException e)
			{
				throw new DataAccessException(e.getMessage());
			}
			finally
			{
				release(null, ps, conn);
			}
		}
	}

	public void beginBatch(String sql)
	{
		Connection conn = tl.get();
		PreparedStatement ps = null;
		try
		{
			ps = conn.prepareStatement(sql);
			sl.set(ps);
			showSQL(sql);
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
	}

	public void addBatch(Object[] params)
	{
		PreparedStatement ps = (PreparedStatement) sl.get();
		try
		{
			setParams(ps, params);
			ps.addBatch();
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
	}

	public int[] excuteBatch()
	{
		PreparedStatement ps = (PreparedStatement) sl.get();
		int[] result = null;
		try
		{
			result = ps.executeBatch();
			return result;
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
	}

	public void endBatch()
	{
		sl.remove();
	}

	public Object scalar(String sql, Object... params)
	{
		Object res = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			setParams(ps, params);
			showSQL(sql, params);
			rs = ps.executeQuery();
			if (rs.next())
			{
				res = rs.getObject(1);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
		finally
		{
			release(rs, ps, conn);
		}
		return res;
	}

	public long count(String sql, Object... params)
	{
		long count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			setParams(ps, params);
			showSQL(sql, params);
			rs = ps.executeQuery();
			if (rs.next())
			{
				count = Long.valueOf(rs.getObject(1).toString());
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
		finally
		{
			release(rs, ps, conn);
		}
		return count;
	}

	public void find(String sql, ResultHandler rh)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			showSQL(sql);
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs != null)
			{
				rh.handle(rs);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
		finally
		{
			release(rs, ps, conn);
		}
	}

	public void find(String sql, Object[] params, ResultHandler rh)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			if (params != null)
			{
				for (int i = 0; i < params.length; i++)
				{
					ps.setObject(i + 1, params[i]);
				}
			}
			showSQL(sql, params);
			rs = ps.executeQuery();
			if (rs != null)
			{
				rh.handle(rs);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
		finally
		{
			release(rs, ps, conn);
		}
	}

	public <T> List<T> find(String sql, RowHandler<T> rh)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			showSQL(sql);
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			List<T> list = null;
			if (rs != null)
			{
				list = new ArrayList<T>();
				while (rs.next())
				{
					list.add(rh.handle(rs));
				}
			}
			return list;
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
		finally
		{
			release(rs, ps, conn);
		}
	}

	public <T> List<T> find(String sql, Object[] params, RowHandler<T> rh)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			if (params != null)
			{
				for (int i = 0; i < params.length; i++)
				{
					ps.setObject(i + 1, params[i]);
				}
			}
			showSQL(sql, params);
			rs = ps.executeQuery();
			List<T> list = null;
			if (rs != null)
			{
				list = new ArrayList<T>();
				while (rs.next())
				{
					list.add(rh.handle(rs));
				}
			}
			return list;
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
		finally
		{
			release(rs, ps, conn);
		}
	}

	public List<R> find(String sql, Object... params)
	{
		return find(sql, params, new RowHandler<R>()
		{
			public R handle(ResultSet rs) throws SQLException
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();
				R r = new R();
				for (int i = 0; i < colCount; i++)
				{
					String key = rsmd.getColumnLabel(i + 1).toLowerCase();
					Object val = rs.getObject(i + 1) != null ? rs.getObject(i + 1) : "";
					r.put(key, val);
				}
				return r;
			}

		});
	}
	
	public ResultSet findBigData(String sql, Object... params)
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			if (params != null)
			{
				for (int i = 0; i < params.length; i++)
				{
					ps.setObject(i + 1, params[i]);
				}
			}
			showSQL(sql, params);
			rs = ps.executeQuery();
		}
		catch (SQLException e)
		{
			release(rs, ps, conn);
			throw new DataAccessException(e.getMessage());
		}
		return rs;
	}

	public R findOne(String sql)
	{
		List<R> list = find(sql);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	public R findOne(String sql, Object[] params)
	{
		List<R> list = find(sql, params);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	public List<R> pager(String sql, int page, int pagesize)
	{
		List<R> rows = new ArrayList<R>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			String databaseProductName = metaData.getDatabaseProductName();
			sql = getPageSql(databaseProductName, sql, page, pagesize);
			showSQL(sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			while (rs.next())
			{
				R r = new R();
				for (int i = 0; i < colCount; i++)
				{
					String key = rsmd.getColumnLabel(i + 1).toLowerCase();
					Object val = rs.getObject(i + 1) != null ? rs.getObject(i + 1) : "";
					r.put(key, val);
				}
				rows.add(r);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
		finally
		{
			release(rs, ps, conn);
		}
		return rows;
	}

	public List<R> pager(String sql, Object[] params, int currPage, int pageSize)
	{
		List<R> list = new ArrayList<R>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			String databaseProductName = metaData.getDatabaseProductName();
			sql = getPageSql(databaseProductName, sql, currPage, pageSize);
			ps = conn.prepareStatement(sql);
			if (params != null)
			{
				for (int i = 0, len = params.length; i < len; i++)
				{
					ps.setObject(i + 1, params[i]);
				}
				showSQL(sql, params);
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			while (rs.next())
			{
				R r = new R();
				for (int i = 0; i < colCount; i++)
				{
					String key = rsmd.getColumnLabel(i + 1).toLowerCase();
					Object val = rs.getObject(i + 1) != null ? rs.getObject(i + 1) : "";
					r.put(key, val);
				}
				list.add(r);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
		finally
		{
			release(rs, ps, conn);
		}
		return list;
	}

	public String[] call(String procName, Object[] params, int outParamNum)
	{
		String[] ret = new String[outParamNum];
		Connection conn = null;
		CallableStatement cs = null;
		int inParamNum = (null != params) ? params.length : 0;
		String procSql = getCallStr(procName, inParamNum, outParamNum);
		try
		{
			conn = getConnection();
			cs = conn.prepareCall(procSql);
			for (int i = 0; i < inParamNum; i++)
			{
				cs.setObject(i + 1, params[i]);
			}
			for (int k = 1; k <= outParamNum; k++)
			{
				cs.registerOutParameter(inParamNum + k, Types.VARCHAR);
			}
			cs.executeQuery();
			for (int k = 1; k <= outParamNum; k++)
			{
				ret[k - 1] = cs.getString(inParamNum + k);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
		finally
		{
			release(null, cs, conn);
		}
		return ret;
	}

	private String getCallStr(String procName, int inParamNum, int outParamNum)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("{call ").append(procName);
		int paramCount = inParamNum + outParamNum;
		if (paramCount > 0)
		{
			sb.append("(");
			for (int i = 0; i < paramCount; i++)
			{
				sb.append("?");
				if (i != paramCount - 1)
				{
					sb.append(",");
				}
			}
			sb.append(")");
		}
		sb.append("}");
		return sb.toString();
	}

	private String getPageSql(String dialect, String sql, int currPage, int pageSize)
	{
		StringBuffer pageSql = new StringBuffer();
		if ("oracle".equalsIgnoreCase(dialect))
		{
			pageSql.append("SELECT * FROM(SELECT FA.*, ROWNUM RN FROM (");
			pageSql.append(sql).append(") FA WHERE ROWNUM <= ");
			pageSql.append(currPage * pageSize).append(") WHERE RN >= ").append((currPage - 1) * pageSize + 1);
		}
		else if ("mysql".equalsIgnoreCase(dialect))
		{
			pageSql.append(sql).append(" limit ").append((currPage - 1) * pageSize).append(",").append(pageSize);
		}
		else
		{
			pageSql.append(sql).append(" limit ").append((currPage - 1) * pageSize).append(",").append(pageSize);
		}
		return pageSql.toString();
	}

	public String getPageSql(String dialect, String sql, List<Object> params, int page, int pageSize)
	{
		StringBuffer pageSql = new StringBuffer(sql);
		if ("oracle".equalsIgnoreCase(dialect))
		{
			int begin = pageSize * (page - 1) + 1;
			int end = pageSize * page;
			String format = "SELECT * FROM(SELECT FA.*, ROWNUM RN FROM (%s) t FA WHERE ROWNUM <= " + end
					+ ") WHERE RN >= " + begin;
			pageSql.replace(0, pageSql.length(), String.format(format, sql));
			params.add(pageSize * page);
			params.add(pageSize * (page - 1) + 1);
		}
		if ("mysql".equalsIgnoreCase(dialect))
		{
			int begin = pageSize * (page - 1);
			pageSql.append(" limit ");
			pageSql.append(begin);
			pageSql.append(",");
			pageSql.append(pageSize);
		}
		return pageSql.toString();
	}

	private void showSQL(String sql, Object... params)
	{
		if (log.isDebugEnabled())
		{
			match(sql, params);
			if (null != params && params.length > 0)
			{
				StringBuffer returnSQL = new StringBuffer();
				int paramNum = params.length;
				String[] subSQL = sql.split("\\?");
				for (int i = 0; i < paramNum; i++)
				{
					returnSQL.append(subSQL[i]);
					if (params[i] instanceof String)
					{
						returnSQL.append("'");
					}
					returnSQL.append(String.valueOf(params[i]));
					if (params[i] instanceof String)
					{
						returnSQL.append("'");
					}
				}
				// 如果问号不是原sql的最后一个字符，则将改问号后的部分添加到returnSQL中
				if (subSQL.length > params.length)
				{
					returnSQL.append(subSQL[subSQL.length - 1]);
				}
				log.debug("SQL==>" + returnSQL.toString());
			}
			else
			{
				log.debug("SQL==>" + sql);
			}
		}
	}

	private void setParams(PreparedStatement ps, Object... params)
	{
		try
		{
			if (null == params)
			{
				return;
			}
			for (int i = 0; i < params.length; i++)
			{
				Object o = params[i];
				if (o != null)
				{
					if (o instanceof java.util.Date)
					{
						java.sql.Timestamp ts = new java.sql.Timestamp(((java.util.Date) o).getTime());
						ps.setTimestamp(i + 1, ts);
					}
					else
					{
						ps.setObject(i + 1, params[i]);
					}
				}
				else
				{
					ps.setNull(i + 1, java.sql.Types.NULL);
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}

	public int getID(String table)
	{
		int id = 1;
		Object currid = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			ps = conn.prepareStatement("select currid from seq where tname=?");
			ps.setString(1, table);
			rs = ps.executeQuery();
			if (rs.next())
			{
				currid = rs.getObject(1);
			}
			if (currid == null)
			{
				PreparedStatement ps1 = conn.prepareStatement("insert into seq(currid,tname) values(?,?)");
				ps1.setInt(1, 1);
				ps1.setString(2, table);
				ps1.executeUpdate();
			}
			else
			{
				id = Integer.parseInt(currid.toString()) + 1;
				PreparedStatement ps2 = conn.prepareStatement("update seq set currid=? where tname=?");
				ps2.setInt(1, id);
				ps2.setString(2, table);
				ps2.executeUpdate();
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage());
		}
		finally
		{
			release(rs, ps, conn);
		}
		return id;
	}

	public void begin()
	{
		try
		{
			log.debug("Transaction begin");
			Connection conn = tl.get();
			if (null == conn)
			{
				conn = getConnection();
				conn.setAutoCommit(false);
				tl.set(conn);
				begintx.set(true);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException("Transaction begin exception!");
		}
	}

	public void commit()
	{
		try
		{
			log.debug("Transaction commit");
			Connection conn = tl.get();
			conn.commit();
		}
		catch (SQLException e)
		{
			throw new DataAccessException("Transaction commit exception!");
		}
	}

	public void close()
	{
		try
		{
			log.debug("Connection close");
			Connection conn = tl.get();
			conn.commit();
			conn.setAutoCommit(true);
			release(null, null, conn);
			tl.remove();
			begintx.set(false);
		}
		catch (SQLException e)
		{
			throw new DataAccessException("Transaction commit exception!");
		}
	}

	public void rollback()
	{
		try
		{
			log.debug("Transaction rollback");
			Connection conn = tl.get();
			conn.rollback();
		}
		catch (SQLException e)
		{
			throw new DataAccessException("Transaction rollback exception!");
		}
	}

	private boolean match(String sql, Object[] params)
	{
		Matcher m = Pattern.compile("(\\?)").matcher(sql);
		int count = 0;
		while (m.find())
		{
			count++;
		}
		return count == params.length;
	}

	public void runScript(Reader reader)
	{
		Connection conn = null;
		try
		{
			StringBuffer script = new StringBuffer();
			List<String> sqlList = new ArrayList<String>();
			BufferedReader lineReader = new BufferedReader(reader);
			String line;
			while ((line = lineReader.readLine()) != null)
			{
				script.append(line);
				script.append("\r\n");
			}
			String[] sqlArr = script.toString().split("(;\\s*\\r\\n)");
			for (int i = 0; i < sqlArr.length; i++)
			{
				String sql = sqlArr[i].replaceAll("--.*", "").trim();
				if (!sql.equals(""))
				{
					sqlList.add(sql);
				}
			}
			conn = getConnection();
			Statement smt = conn.createStatement();
			for (String sql : sqlList)
			{
				smt.addBatch(sql);
			}
			log.debug("runScript\r\n========begin========\r\n" + script + "=========end=========\r\n");
			smt.executeBatch();
			smt.close();
		}
		catch (Exception e)
		{
			throw new DataAccessException("executing script cause", e);
		}
		finally
		{
			release(null, null, conn);
		}
	}

	public boolean existTable(String table)
	{
		boolean result = false;
		Connection conn = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			String[] types = { "TABLE" };
			rs = conn.getMetaData().getTables(null, null, table, types);
			if (rs.next())
			{
				result = true;
			}
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		finally
		{
			release(rs, null, conn);
		}
		return result;
	}

	public String getDriver()
	{
		BasicDataSource bds = (BasicDataSource) ds;
		return bds.getDriverClassName();
	}

	public String getUrl()
	{
		BasicDataSource bds = (BasicDataSource) ds;
		return bds.getUrl();
	}

	public String getUsername()
	{
		BasicDataSource bds = (BasicDataSource) ds;
		return bds.getUsername();
	}

	public String getPassword()
	{
		BasicDataSource bds = (BasicDataSource) ds;
		return bds.getPassword();
	}

	public void state()
	{
		BasicDataSource bds = (BasicDataSource) ds;
		int active = bds.getNumActive();
		int idle = bds.getNumIdle();
		System.out.println(active);
		System.out.println(idle);
	}

	public static void main(String[] args)
	{
		DB dao = DB.getInstance();
		List<R> r = dao.find("select * from users");
		System.out.println(r);
		// dao.update("insert users values(280, '123', '123', 2, '2015-01-15 14:44:50', 'aa@123.com','123456','memo')");
		// dao.state();
	}
}