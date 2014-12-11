package org.rzy.web.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class MongoAppender extends UnsynchronizedAppenderBase<ILoggingEvent>
{
	Mongo mongo = null;
	private String host = null;
	private String db = null;
	private String collection = null;
	private boolean logToLocal = false;

	@Override
	protected void append(ILoggingEvent eventObject)
	{
		DBCollection dbCollection = mongo.getDB(db).getCollection(collection);
		String msg = eventObject.getFormattedMessage();
		String[] arr = msg.split("\\|");
		String user = arr[0];
		String ip = arr[1];
		String sid = arr[2];
		String memo = arr[3];
		String result = arr[4];
		BasicDBObject logEntry = new BasicDBObject();
		logEntry.append("user", user);
		logEntry.append("ip", ip);
		logEntry.append("sid", sid);
		logEntry.append("result", result);
		logEntry.append("memo", memo);
		//logEntry.append("logger", eventObject.getLoggerName());
		logEntry.append("thread", eventObject.getThreadName());
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String time = df.format(new Date(eventObject.getTimeStamp()));
		logEntry.append("time", time);
		//logEntry.append("level", eventObject.getLevel().toString());
		//logEntry.append("pid", getPid());
		//logEntry.append("ip", getIp());
		Map<String, String> data = eventObject.getMDCPropertyMap();
		for (String key : data.keySet())
		{
			logEntry.append(key, data.get(key));
		}
		dbCollection.insert(logEntry);
		if (logToLocal)
		{
			System.out.println(eventObject.getFormattedMessage());
		}
	}

	@Override
	public void start()
	{
		try
		{
			mongo = new Mongo(host);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		super.start();
	}

	@Override
	public void stop()
	{
		if (!isStarted())
			return;
		super.stop();

	}

	public void setHost(String host)
	{
		this.host = host;
	}

	public void setDb(String db)
	{
		this.db = db;
	}

	public void setCollection(String collection)
	{
		this.collection = collection;
	}

	public void setLogToLocal(boolean logToLocal)
	{
		this.logToLocal = logToLocal;
	}
}
