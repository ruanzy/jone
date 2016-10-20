package com.rz.monitor;

import java.util.Random;

public class CpuMonitor implements Monitor
{

	public double getData()
	{
		Random r = new Random();
		double v = r.nextDouble() * 100;
		return v;
	}
}
