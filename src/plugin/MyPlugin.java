package plugin;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rz.dao.DB;
import com.rz.dao.DBs;
import com.rz.dao.DataAccessException;
import com.rz.web.Plugin;

public class MyPlugin implements Plugin
{
	static final Logger logger = LoggerFactory.getLogger(MyPlugin.class);

	@Override
	public void start()
	{
		initSQL();
	}

	@Override
	public void stop()
	{
	}

	private void initSQL()
	{
		String table = "USERS";
		String sqlScript = "init.sql";
		String sql = "SELECT COUNT(*) FROM  information_schema.tables where table_schema='PUBLIC' and table_name=?";
		Object obj = null;
		DB db = null;
		try
		{
			db = DBs.getDB("h2");;
			obj = db.scalar(sql, new Object[] { table });
		}
		catch (DataAccessException e)
		{
			e.printStackTrace();
			throw new DataAccessException(e.getMessage());
		}
		boolean isexsist = (obj != null) && (Integer.valueOf(obj.toString()) > 0);
		if (!isexsist)
		{
			logger.info("execute " + sqlScript + "...");
			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(sqlScript);
			try
			{
				db.runScript(new InputStreamReader(is, "UTF8"));
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
	}
}
