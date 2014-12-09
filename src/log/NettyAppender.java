package log;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

public class NettyAppender extends AppenderBase<ILoggingEvent>
{

	static ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
			Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
	static ChannelPipelineFactory cf = new ChannelPipelineFactory()
	{
		public ChannelPipeline getPipeline()
		{
			ChannelPipeline pipeline = Channels.pipeline();
			return pipeline;
		}
	};

	PatternLayoutEncoder encoder;
	int port;
	String ip;
	Channel channel;

	public NettyAppender()
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

	public int getPort()
	{
		return port;
	}

	public void setPort(int port)
	{
		this.port = port;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	@Override
	public void start()
	{
		if (this.encoder == null)
		{
			addError("no layout of udp appender");
			return;
		}
		bootstrap.setPipelineFactory(cf);
		ChannelFuture future = bootstrap.connect(new InetSocketAddress(ip, port));
		channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess())
		{
			throw new RuntimeException("Connect to server " + ip + ":" + port + " is refused");
		}
		super.start();
	}

	@Override
	protected void append(ILoggingEvent event)
	{
		byte[] buf = encoder.getLayout().doLayout(event).trim().getBytes();
		try
		{
			ChannelBuffer buffer = ChannelBuffers.buffer(buf.length);
			buffer.writeBytes(buf);
			channel.write(buffer);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void stop()
	{
		channel.close().awaitUninterruptibly();
		super.stop();
	}

}
