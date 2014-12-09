package org.rzy.web.log;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class LogStat
{
	static
	{
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ChannelPipelineFactory()
		{
			public ChannelPipeline getPipeline()
			{
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("handler", new ServerHandler());
				return pipeline;
			}
		});
		bootstrap.bind(new InetSocketAddress(9000));
	}

	public static void main(String[] args) throws Exception
	{
		new LogStat();
	}

}

class ServerHandler extends SimpleChannelHandler
{

	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{

	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		ChannelBuffer buffer = (ChannelBuffer) e.getMessage();
		System.out.println(buffer.toString(Charset.defaultCharset()));
	}

	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	{
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}