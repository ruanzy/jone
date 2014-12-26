package org.rzy.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.rzy.dao.MongoDao;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.mongodb.BasicDBObject;

public abstract class MongoAppender extends UnsynchronizedAppenderBase<ILoggingEvent>
{
	private String collection = null;

	@Override
	protected void append(ILoggingEvent eventObject)
	{
		String msg = eventObject.getFormattedMessage();
		String thread = eventObject.getThreadName();
		BasicDBObject logEntry = new BasicDBObject();
		// logEntry.append("logger", eventObject.getLoggerName());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String time = df.format(new Date(eventObject.getTimeStamp()));
		logEntry.append("time", time);
		logEntry.append("thread", thread);
		Map<String, String> msgmap = MsgParse(msg);
		logEntry.putAll(msgmap);
		// logEntry.append("level", eventObject.getLevel().toString());
		// logEntry.append("pid", getPid());
		// logEntry.append("ip", getIp());
		Map<String, String> data = eventObject.getMDCPropertyMap();
		for (String key : data.keySet())
		{
			logEntry.append(key, data.get(key));
		}
		MongoDao.insert(collection, logEntry);
	}

	@Override
	public void start()
	{
		super.start();
	}

	@Override
	public void stop()
	{
		if (!isStarted())
			return;
		super.stop();

	}

	public void setCollection(String collection)
	{
		this.collection = collection;
	}

	public abstract Map<String, String> MsgParse(String msg);
}
