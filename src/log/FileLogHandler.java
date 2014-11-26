package log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.rzy.web.WebUtil;
import org.rzy.web.log.Log;
import org.rzy.web.log.LogHandler;
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
		File f = new File(WebUtil.getWebRoot(), "log/log.txt");
		try
		{
			RandomAccessFile rf = new RandomAccessFile(f, "rw");
			long fileLength = rf.length();
			rf.seek(fileLength);
			rf.write(logs.toString().getBytes("UTF-8"));
			rf.write(System.getProperty("line.separator").getBytes("UTF-8"));
			rf.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
