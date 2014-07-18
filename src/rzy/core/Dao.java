package rzy.core;

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
	private static DataSource ds = null;
	private static ThreadLocal<Connection> tl = new ThreadLocal<Connection>();
	private static Properties prop = new Properties();
	static boolean showsql = false;
	static Logger log = LoggerFactory.getLogger(Dao.class);

	private Dao()
	{

	}

	static
	{
		InputStream is = null;
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
	
	public static synchronized void showSql(boolean isShowsql){
		showsql = isShowsql;
	}

	private static synchronized Connection getConnection()
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

	private static void close(ResultSet rs, PreparedStatement ps, Connection conn)
	{
		closeResultSet(rs);
		closeStatement(ps);
		closeConnection(conn);
	}

	private static void closeConnection(Connection conn)
	{
		try
		{
			if (conn != null)
			{
				conn.close();
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	private static void closeStatement(Statement st)
	{
		try
		{
			if (st != null)
			{
				st.close();
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	private static void closeResultSet(ResultSet rs)
	{
		try
		{
			if (rs != null)
			{
				rs.close();
			}
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
	}

	public static int update(String sql)
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

	public static int update(String sql, Object[] params)
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

	public static int[] batchUpdate(String sql, List<Object[]> params)
	{
		int[] result = new int[params.size()];
		Connection conn = tl.get();
		PreparedStatement ps = null;
		try
		{
			for (int i = 0, l = params.size(); i < l; i++)
			{
				showSQL(sql, params.get(i));
			}
			ps = conn.prepareStatement(sql);
			for (Object[] objects : params)
			{
				for (int i = 0; i < objects.length; i++)
				{
					ps.setObject(i + 1, objects[i]);
				}
				ps.addBatch();
			}
			result = ps.executeBatch();
		}
		catch (SQLException e)
		{
			throw new DataAccessException(e.getMessage(), e);
		}
		return result;
	}

	public static Object scalar(String sql)
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

	public static Object scalar(String sql, Object[] params)
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

	public static void find(String sql, ResultHandler rh)
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
			rh.handler(rs);
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

	public static List<Map<String, Object>> find(String sql)
	{
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		find(sql, new ResultHandler()
		{

			public void handler(ResultSet rs) throws SQLException
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

	public static void find(String sql, Object[] params, ResultHandler rh)
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
			rh.handler(rs);
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

	public static List<Map<String, Object>> find(String sql, Object[] params)
	{
		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		find(sql, new ResultHandler()
		{
			public void handler(ResultSet rs) throws SQLException
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

	public static Map<String, Object> findOne(String sql)
	{
		List<Map<String, Object>> list = find(sql);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	public static Map<String, Object> findOne(String sql, Object[] params)
	{
		List<Map<String, Object>> list = find(sql, params);
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	public static List<Map<String, Object>> pager(String sql, int currPage, int pageSize)
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

	public static List<Map<String, Object>> pager(String sql, Object[] params, int currPage, int pageSize)
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

	public static String[] call(String procName, Object[] params, int outParamNum)
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

	private static String getCallStr(String procName, int inParamNum, int outParamNum)
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

	private static String getPageSql(String dialect, String sql, int currPage, int pageSize)
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

	private static void showSQL(String sql)
	{
		if (log.isDebugEnabled())
		{
			if(showsql){
				log.debug("SQL==>" + sql);
			}
		}
	}

	private static void showSQL(String sql, Object[] params)
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

	public static int getID(String table)
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
				ps = conn.prepareStatement("insert into seq(currid,tname) values(?,?)");
				ps.setInt(1, 1);
				ps.setString(2, table);
				ps.executeUpdate();
			}
			else
			{
				id = Integer.parseInt(currid.toString()) + 1;
				ps = conn.prepareStatement("update seq set currid=? where tname=?");
				ps.setInt(1, id);
				ps.setString(2, table);
				ps.executeUpdate();
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

	public static void begin()
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

	public static void commit()
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

	public static void rollback()
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

	public static void runScript(Reader reader)
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

	public static boolean existTable(String table)
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
		Dao.find("select * from log");
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