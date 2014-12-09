package log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest
{
	static Logger log = LoggerFactory.getLogger(LogTest.class);

	public static void main(String[] args)
	{
		System.out.println("logtest");
		log.debug("debug...");
	}
}
