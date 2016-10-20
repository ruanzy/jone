package com.rz.monitor;

import java.util.Random;

public class CpuMonitor implements Monitor
{

	public double getData()
	{
		Random r = new Random();
		double v = r.nextDouble() * 100;
		java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");  
		
		return Double.valueOf(df.format(v));
	}
}
