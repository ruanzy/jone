package com.rz.monitor;

import java.util.HashMap;
import java.util.Map;

public class CpuMonitor implements Monitor
{

	public double getData(String ip)
	{
		Map<String, String> node = getNode(ip);
		String host = node.get("ip");
		String user = node.get("user");
		String password = node.get("password");
		int port = Integer.valueOf(node.get("port"));
		double v = DataUtil.cpu(host, user, password, port);
		return v;
	}

	public Map<String, String> getNode(String ip)
	{
		Map<String, String> node = new HashMap<String, String>();
		node.put("ip", ip);
		node.put("user", "root");
		node.put("password", "!qaz2wsx");
		node.put("port", "22");
		return node;
	}
}
