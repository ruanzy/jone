package com.rz.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Test
{
	public static void main(String[] args)
	{
		String rrdDir = "d:/rrd";
		final MonitorManger m = MonitorManger.getInstance();
		m.setRrdDir(rrdDir);
		m.ds("cpu", new CpuMonitor());
		final String ip = "11.0.1.83";
		final String ds = "cpu";
		List<Archive> archives = new ArrayList<Archive>();
		Archive a1 = new Archive("AVERAGE", 1, 240 * 720);
		Archive a2 = new Archive("AVERAGE", 720, 240);
		archives.add(a1);
		archives.add(a2);
		m.addMonitor(ip, ds, archives);
		new Timer().schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				double[] vs = m.fetch(ip, ds, 1, 1);
				for (int i = 0; i < vs.length; i++)
				{
					System.out.println("read=" + vs[i]);
				}
			}
		}, 5000, 5000);
	}
}
