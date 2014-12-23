package log;

import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;

public class DefaultLogHandler implements LogHandler
{
	Logger logger = LoggerFactory.getLogger(DefaultLogHandler.class);

	public void handler(Log log)
	{
		StringBuffer logs = new StringBuffer();
		String user = log.getUser();
		String ip = log.getIP();
		String time = log.getTime();
		String sid = log.getSid();
		String op = Util.getOP(sid);
		Object[] args = log.getArgs();
		String requestBody = JSON.toJSONString(args);
		logs.append(user).append("|");
		logs.append(ip).append("|");
		logs.append(time).append("|");
		logs.append(op).append("|");
		logs.append(sid).append("|");
		logs.append(1).append("|");
		logs.append(requestBody);
		logger.debug(logs.toString());
		String logfile = System.getProperty("logDir") + "log.txt";
		FileWriter fw = null;
		try
		{
			fw = new FileWriter(logfile);
			fw.write(logs.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(fw);
		}
	}
}
