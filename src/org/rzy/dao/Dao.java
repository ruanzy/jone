package org.rzy.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Dao
{
	private DataSource ds = null;
	private ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
	private ThreadLocal<Statement> sl = new ThreadLocal<Statement>();
	boolean showsql = false;
	Logger log = LoggerFactory.getLogger(Dao.class);

	private static class SingletonHolder
	{
		private final static Dao INSTANCE = new Dao();
	}

	public static Dao getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	private Dao()
	{
		InputStream is = null;
		Properties prop = new Properties();
		try
		{
			is = Dao.class.getClassLoader().getResourceAsStream("db.properties");
			if (is == null)
			{
				is = new FileInputStream("db.properties");
			}
			prop.load(is);
			ds = BasicDataSourceFactory.createDataSource(prop);
		}
		catch (Exception e)
		{
			log.debug("Create DataSource Exception!");
			throw new DataAccessException("Create DataSource Exception!", e);
		}
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
			throw new DataAccessException("Get the database connection failed!", e);
		}
		return conn;
	}

	private void close(Object... sqlObjs)
	{
		try
		{
			for (Object obj : sqlObjs)
			{
				if (obj == null)
				{
					continue;
				}
				if (obj instanceof Connection)
				{
					((Connection) obj).close();
				}
				else if (obj instanceof Statement)
				{
					((Statement) obj).close();
				}
				else if (obj instanceof ResultSet)
				{
					((ResultSet) obj).close();
				}
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	public int update(String sql)
	{
		int result = 0;
		PreparedStatement ps = null;
		try
		{
			Connection conn = tl.get();
			ps = conn.prepareStatement(sql);
			result = ps.executeUpdate();
			showSQL(sql);
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
		return result;
	}

	public int update(String sql, Object[] params)
	{
		int result = 0;
		PreparedStatement ps = null;
		try
		{
			Connection conn = tl.get();
			ps = conn.prepareStatement(sql);
			if (params != null)
			{
				for (int i = 0; i < params.length; i++)
				{
					ps.setObject(i + 1, params[i]);
				}
				showSQL(sql, params);
			}
			result = ps.executeUpdate();
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
		return result;
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
			throw new DataAccessException(e.getMessage(), e);
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
			throw new DataAccessException(e.getMessage(), e);
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
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	public void endBatch()
	{
		sl.remove();
	}

	public Object scalar(String sql)
	{
		Object res = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			showSQL(sql);
			conn = getConnection();
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next())
			{
				res = rs.getObject(1);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
		finally
		{
			close(rs, ps, conn);
		}
		return res;
	}

	public Object scalar(String sql, Object[] params)
	{
		Object res = null;
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
				showSQL(sql, params);
			}
			rs = ps.executeQuery();
			if (rs.next())
			{
				res = rs.getObject(1);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
		finally
		{
			close(rs, ps, conn);
		}
		return res;
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
			rh.handle(rs);
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
		finally
		{
			close(rs, ps, conn);
		}
	}

	public List<Map<String, Object>> find(String sql)
	{
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		find(sql, new ResultHandler()
		{
			public void handle(ResultSet rs) throws SQLException
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();
				while (rs.next())
				{
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 0; i < colCount; i++)
					{
						String key = rsmd.getColumnLabel(i + 1).toLowerCase();
						Object val = rs.getObject(i + 1) != null ? rs.getObject(i + 1) : "";
						map.put(key, val);
					}
					list.add(map);
				}
			}
		});
		return list;
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
				showSQL(sql, params);
			}
			rs = ps.executeQuery();
			rh.handle(rs);
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
		finally
		{
			close(rs, ps, conn);
		}
	}

	public List<Map<String, Object>> find(String sql, Object[] params)
	{
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		find(sql, params, new ResultHandler()
		{
			public void handle(ResultSet rs) throws SQLException
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCount = rsmd.getColumnCount();
				while (rs.next())
				{
					Map<String, Object> map = new HashMap<String, Object>();
					for (int i = 0; i < colCount; i++)
					{
						String key = rsmd.getColumnLabel(i + 1).toLowerCase();
						Object val = rs.getObject(i + 1) != null ? rs.getObject(i + 1) : "";
						map.put(key, val);
					}
					list.add(map);
				}
			}

		});
		return list;
	}

	public Map<String, Object> findOne(String sql)
	{
		List<Map<String, Object>> list = find(sql);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	public Map<String, Object> findOne(String sql, Object[] params)
	{
		List<Map<String, Object>> list = find(sql, params);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	public List<Map<String, Object>> pager(String sql, int currPage, int pageSize)
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			DatabaseMetaData metaData = conn.getMetaData();
			String databaseProductName = metaData.getDatabaseProductName();
			sql = getPageSql(databaseProductName, sql, currPage, pageSize);
			showSQL(sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			while (rs.next())
			{
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < colCount; i++)
				{
					String key = rsmd.getColumnLabel(i + 1).toLowerCase();
					Object val = rs.getObject(i + 1) != null ? rs.getObject(i + 1) : "";
					map.put(key, val);
				}
				list.add(map);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
		finally
		{
			close(rs, ps, conn);
		}
		return list;
	}

	public List<Map<String, Object>> pager(String sql, Object[] params, int currPage, int pageSize)
	{
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
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
				for (int i = 0; i < params.length; i++)
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
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < colCount; i++)
				{
					String key = rsmd.getColumnLabel(i + 1).toLowerCase();
					Object val = rs.getObject(i + 1) != null ? rs.getObject(i + 1) : "";
					map.put(key, val);
				}
				list.add(map);
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
		finally
		{
			close(rs, ps, conn);
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
			throw new DataAccessException(e.getMessage(), e);
		}
		finally
		{
			close(null, cs, conn);
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
		StringBuffer pageSql = new StringBuffer(0);
		if ("oracle".equalsIgnoreCase(dialect))
		{
			pageSql.append("SELECT * FROM(SELECT FA.*, ROWNUM RN FROM (");
			pageSql.append(sql).append(") FA WHERE ROWNUM <= ");
			pageSql.append(currPage * pageSize).append(") WHERE RN >= ").append((currPage - 1) * pageSize + 1);
		}
		if ("mysql".equalsIgnoreCase(dialect))
		{
			pageSql.append(sql).append(" limit ").append((currPage - 1) * pageSize).append(",").append(pageSize);
		}
		return pageSql.toString();
	}

	private void showSQL(String sql)
	{
		if (log.isDebugEnabled())
		{
			if (showsql)
			{
				log.debug("SQL==>" + sql);
			}
		}
	}

	private void showSQL(String sql, Object[] params)
	{
		if (log.isDebugEnabled())
		{
			if (null != params)
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
		}
	}

	private void setParams(PreparedStatement ps, Object[] params)
	{
		try
		{
			if (params != null)
			{
				// log.debug("set parameters");
				for (int i = 0; i < params.length; i++)
				{
					Object o = params[i];
					if (o != null)
					{
						ps.setObject(i + 1, params[i]);
					}
					else
					{
						ps.setNull(i + 1, java.sql.Types.NULL);
					}
					// log.debug("{}:{}", i + 1, params[i]);
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
			throw new DataAccessException(e.getMessage(), e);
		}
		finally
		{
			close(rs, ps, conn);
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
			conn.setAutoCommit(true);
			close(null, null, conn);
			tl.remove();
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
			conn.setAutoCommit(true);
			close(null, null, conn);
			tl.remove();
		}
		catch (SQLException e)
		{
			throw new DataAccessException("Transaction rollback exception!");
		}
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
				log.debug("SQL==>" + sql);
			}
			smt.executeBatch();
			smt.close();
		}
		catch (Exception e)
		{
			throw new DataAccessException("executing script cause", e);
		}
		finally
		{
			close(null, null, conn);
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
			rs = conn.getMetaData().getTables(null, null, table, null);
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
			close(rs, null, conn);
		}
		return result;
	}

	public static void main(String[] args)
	{
		Dao dao = Dao.getInstance();
		dao.find("select * from log");
	}
}

class DataAccessException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public DataAccessException()
	{
		super();
	}

	public DataAccessException(String message)
	{
		super(message);
	}

	public DataAccessException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DataAccessException(Throwable cause)
	{
		super(cause);
	}
}