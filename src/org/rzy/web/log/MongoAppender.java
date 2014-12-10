package org.rzy.web.log;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
		BasicDBObject logEntry = new BasicDBObject();
		logEntry.append("message", eventObject.getFormattedMessage());
		logEntry.append("logger", eventObject.getLoggerName());
		logEntry.append("thread", eventObject.getThreadName());
		logEntry.append("timestamp", new Date(eventObject.getTimeStamp()));
		logEntry.append("level", eventObject.getLevel().toString());
		logEntry.append("pid", getPid());
		logEntry.append("ip", getIp());
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

	private String getIp()
	{
		try
		{
			return InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
			return "UNKNOW_HOST";
		}
	}

	private String getPid()
	{
		return ManagementFactory.getRuntimeMXBean().getName();
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
