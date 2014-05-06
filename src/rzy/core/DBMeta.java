package rzy.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DBMeta
{
	private static Properties prop = new Properties();
	static Logger log = LoggerFactory.getLogger(DBMeta.class);

	private DBMeta()
	{

	}

	static
	{
		InputStream is = null;
		try
		{
			is = DBMeta.class.getClassLoader().getResourceAsStream("db.properties");
			if (is == null)
			{
				is = new FileInputStream("db.properties");
			}
			prop.load(is);
			String driver = (String) prop.get("driverClassName");
			Class.forName(driver);
		}
		catch (Exception e)
		{
			log.debug("Create DataSource Exception!");
			throw new DataAccessException("Create DataSource Exception!", e);
		}
	}

	private static synchronized Connection getConnection()
	{
		Connection conn = null;
		try
		{
			String url = (String) prop.get("url");
			String username = (String) prop.get("username");
			String password = (String) prop.get("password");
			conn = DriverManager.getConnection(url, username, password);
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

	public static Set<String> getTable()
	{
		Set<String> tables = new HashSet<String>();
		Connection conn = null;
		ResultSet rs = null;
		try
		{
			conn = getConnection();
			rs = conn.getMetaData().getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next())
			{
				String tableName = rs.getString("TABLE_NAME");
				tables.add(tableName);
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
		return tables;
	}

	public static void export(String table, String dir)
	{
		StringBuffer sb = new StringBuffer();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		FileWriter fw = null;
		try
		{
			File d = new File(dir);
			if (!d.exists())
			{
				d.mkdirs();
			}
			File f = new File(dir, table + ".txt");
			f.createNewFile();
			fw = new FileWriter(f);
			conn = getConnection();
			ps = conn.prepareStatement("select * from " + table);
			rs = ps.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			int l = data.getColumnCount();
			while (rs.next())
			{
				sb.append("insert into ").append(table).append(" values(");
				for (int i = 0; i < l; i++)
				{
					String columnname = data.getColumnName(i + 1);
					String typename = data.getColumnTypeName(i + 1);
					if ("CHAR".equalsIgnoreCase(typename) || "VARCHAR".equalsIgnoreCase(typename) || "Date".equalsIgnoreCase(typename))
					{
						sb.append("'");
					}
					sb.append(rs.getObject(columnname));
					if ("CHAR".equalsIgnoreCase(typename) || "VARCHAR".equalsIgnoreCase(typename) || "Date".equalsIgnoreCase(typename))
					{
						sb.append("'");
					}
					if (i != l - 1)
					{
						sb.append(",");
					}
				}
				sb.append(");\r\n");
			}
			fw.append(sb);
		}
		catch (Exception e)
		{
			throw new DataAccessException(e);
		}
		finally
		{
			try
			{
				fw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			close(rs, ps, conn);
		}
	}
}