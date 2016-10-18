package com.rz.monitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jrobin.core.DsTypes;
import org.jrobin.core.FetchData;
import org.jrobin.core.FetchRequest;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdDef;
import org.jrobin.core.Sample;
import org.jrobin.core.Util;
import org.quartz.Job;

public class MonitorUtil
{
	static String RRDDIR = (String) Config.getValue("rrdDir");
	static int STEP = Integer.valueOf(Config.getValue("step").toString());

	public static void addMonitor(String ip, String ds, List<Archive> archives)
	{
		createRrd(ip, ds, archives);
		addJob(ip, ds);
	}

	public static void pauseMonitor(String ip, String ds)
	{
		JobInfo job = new JobInfo();
		job.setName(ip + "_" + ds);
		job.setGroup("group");
		JobManager.removeJob(job);
	}

	private static void createRrd(String ip, String ds, List<Archive> archives)
	{
		RrdDb rrdDb = null;
		try
		{
			String fn = ip + "_" + ds + ".rrd";
			File f = new File(RRDDIR, fn);
			if (!f.exists())
			{
				if (!f.getParentFile().exists())
				{
					f.getParentFile().mkdirs();
				}
				long timeStart = Util.getTimestamp();
				RrdDef rrdDef = new RrdDef(f.getPath(), timeStart - 1, STEP);
				rrdDef.addDatasource(ds, DsTypes.DT_GAUGE, 2 * STEP, 0, Double.NaN);
				for (Archive archive : archives)
				{
					String type = archive.getType();
					int steps = archive.getSteps();
					int rows = archive.getRows();
					rrdDef.addArchive(type, 0.5, steps, rows);
				}
				rrdDb = RrdDbPool.getInstance().requestRrdDb(rrdDef);
			}
		}
		catch (Exception e)
		{

		}
		finally
		{
			if (null != rrdDb)
			{
				try
				{
					rrdDb.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private static void addJob(String ip, String ds)
	{
		JobInfo job = new JobInfo();
		job.setName(ip + "_" + ds);
		job.setGroup("group");
		job.setCron("0/" + STEP + " 0 0 * * ?");
		job.setJobClass(getJob(ds));
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("ip", ip);
		job.setDataMap(dataMap);
		JobManager.addJob(job);
	}

	public static void writeData(String ip, String ds, Double value, long time)
	{
		RrdDb rrdDb = null;
		try
		{
			String fn = ip + "_" + ds + ".rrd";
			File f = new File(RRDDIR, fn);
			rrdDb = RrdDbPool.getInstance().requestRrdDb(f.getPath());
			long lastUpdateTime = rrdDb.getLastUpdateTime();
			time = Util.normalize(time, STEP);
			if (time > lastUpdateTime + 1)
			{
				Sample sample = rrdDb.createSample(time);
				rrdDb.setInfo("T=" + time);
				if (value == null)
					value = Double.NaN;
				sample.setValue(ds, value);
				sample.update();
			}
		}
		catch (Exception e)
		{

		}
		finally
		{
			if (null != rrdDb)
			{
				try
				{
					rrdDb.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static Map<String, List<Double>> fetchRrdData(String rrdPath, List<String> dss, int size, int steps)
	{
		Map<String, List<Double>> dsData = new HashMap<String, List<Double>>();
		RrdDb rrdDb = null;
		try
		{
			// open the file
			rrdDb = RrdDbPool.getInstance().requestRrdDb(rrdPath);

			long seconds = ((size + 1) * steps * STEP);
			// 计算开始结束时间
			org.jrobin.core.Archive archive = rrdDb.getArchive("AVERAGE", steps);
			long archStart = archive.getStartTime() - archive.getArcStep();
			long archEnd = archive.getEndTime();
			long lastTime = rrdDb.getLastUpdateTime();
			long fetchStartTime = lastTime - seconds;
			long fetchEndTime = lastTime;
			if (fetchStartTime < archStart)
			{
				fetchStartTime = archStart;
			}
			if (fetchEndTime > archEnd)
			{
				fetchEndTime = archEnd;
			}

			if (fetchStartTime > fetchEndTime)
			{
				List<Double> dsValueList = new ArrayList<Double>();
				for (int i = 0; i < size; ++i)
				{
					dsValueList.add(Double.NaN);
				}
				for (String ds : dss)
				{
					dsData.put(ds, dsValueList);
				}
			}
			else
			{
				// create fetch request using the database reference
				FetchRequest request = rrdDb.createFetchRequest("AVERAGE", fetchStartTime, fetchEndTime, steps * STEP);
				FetchData fetchData = request.fetchData();

				// 循环获取数据
				for (String ds : dss)
				{
					List<Double> dsValueList = new ArrayList<Double>();
					dsData.put(ds, dsValueList);
					int index = size - 1;
					double[] data = getDoubleNaNArray(size);
					double[] dsValues = fetchData.getValues(ds);
					boolean skip = true;
					for (int i = dsValues.length - 1; i >= 0 && index >= 0; --i)
					{
						// 跳过最开始的NAN值
						if (skip && Double.isNaN(dsValues[i]))
						{
							continue;
						}
						if (Double.isNaN(dsValues[i]))
						{
							data[index--] = dsValues[i];
						}
						else
						{
							data[index--] = Math.round(dsValues[i] * 100) / 100d;
						}
						skip = false;
					}
					for (double d : data)
					{
						dsValueList.add(d);
					}
				}
			}

		}
		catch (Exception e)
		{

		}
		finally
		{
			if (null != rrdDb)
			{
				try
				{
					rrdDb.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
		return dsData;
	}

	private static double[] getDoubleNaNArray(int size)
	{
		double[] data = new double[size];
		for (int i = 0; i < size; ++i)
		{
			data[i] = Double.NaN;
		}
		return data;
	}

	public static Class<? extends Job> getJob(String ds)
	{
		if ("cpu".equals(ds))
		{
			return CpuJob.class;
		}
		else if ("mem".equals(ds))
		{
			return MemJob.class;
		}
		else
		{
			return null;
		}
	}

	public static void main(String[] args)
	{
		String ip = "11.0.1.83";
		List<Archive> archives = new ArrayList<Archive>();
		Archive a1 = new Archive("AVERAGE", 1, 240 * 720);
		Archive a2 = new Archive("AVERAGE", 720, 240);
		archives.add(a1);
		archives.add(a2);
		MonitorUtil.addMonitor(ip, "cpu", archives);
		MonitorUtil.addMonitor(ip, "mem", archives);
	}
}
