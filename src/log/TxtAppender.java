package log;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import org.rzy.web.WebUtil;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class TxtAppender extends AppenderBase<ILoggingEvent>
{
	String file;
	PatternLayoutEncoder encoder;

	public TxtAppender()
	{

	}

	public PatternLayoutEncoder getEncoder()
	{
		return encoder;
	}

	public void setEncoder(PatternLayoutEncoder encoder)
	{
		this.encoder = encoder;
	}

	public String getFile()
	{
		return file;
	}

	public void setFile(String file)
	{
		this.file = file;
	}

	@Override
	public void start()
	{
		if (this.encoder == null)
		{
			addError("no layout of udp appender");
			return;
		}
		encoder.setCharset(Charset.forName("UTF-8"));
		super.start();
	}

	@Override
	protected void append(ILoggingEvent event)
	{
		byte[] buf = encoder.getLayout().doLayout(event).getBytes();
		File f = new File(WebUtil.getWebRoot(), file);
		try
		{
			RandomAccessFile rf = new RandomAccessFile(f, "rw");
			long fileLength = rf.length();
			rf.seek(fileLength);
			rf.write(buf);
			rf.write(System.getProperty("line.separator").getBytes("UTF-8"));
			rf.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void stop()
	{
		super.stop();
	}

}
