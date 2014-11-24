package log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.rzy.web.Log;
import org.rzy.web.LogHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;

public class FileLogHandler implements LogHandler
{
	Logger logger = LoggerFactory.getLogger(FileLogHandler.class);

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
		File f = new File(System.getProperty("root"), "log/log.txt");
		FileWriter fw = null;
		BufferedWriter bw = null;
		try
		{
			fw = new FileWriter(f, true);
			bw = new BufferedWriter(fw);
			bw.write(logs.toString());
			bw.write(13);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.closeQuietly(bw);
			IOUtils.closeQuietly(fw);
		}
	}
}
