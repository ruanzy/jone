package com.rz.monitor;

import java.io.File;
import java.util.List;
import org.jrobin.core.DsTypes;
import org.jrobin.core.FetchData;
import org.jrobin.core.FetchRequest;
import org.jrobin.core.RrdDb;
import org.jrobin.core.RrdDbPool;
import org.jrobin.core.RrdDef;
import org.jrobin.core.Sample;
import org.jrobin.core.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RrdUtil
{
	private static final Logger logger = LoggerFactory.getLogger(RrdUtil.class);
	private String rrdDir;
	private int step = 5;
	private RrdDbPool pool = null;

	private static class SingletonHolder
	{
		private final static RrdUtil INSTANCE = new RrdUtil();
	}

	public static RrdUtil getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	public RrdUtil()
	{
		try
		{
			pool = RrdDbPool.getInstance();
		}
		catch (Exception e)
		{
		}
	}

	public void setStep(int step)
	{
		this.step = step;
	}

	public int getStep()
	{
		return step;
	}

	public void setRrdDir(String rrdDir)
	{
		File f = new File(rrdDir);
		if (!f.exists())
		{
			f.mkdirs();
		}
		this.rrdDir = rrdDir;
	}

	public String getRrdDir()
	{
		return rrdDir;
	}

	public void createRrd(String rrd, String ds, List<Archive> archives)
	{
		if (null == rrdDir)
		{
			logger.debug("Please set rrdDir!");
			return;
		}
		RrdDb rrdDb = null;
		try
		{
			File f = new File(rrdDir, rrd);
			if (!f.exists())
			{
				long timeStart = Util.getTime();
				RrdDef rrdDef = new RrdDef(f.getPath(), timeStart - 1, step);
				rrdDef.addDatasource(ds, DsTypes.DT_GAUGE, 2 * step, 0, Double.NaN);
				for (Archive archive : archives)
				{
					String type = archive.getType();
					int steps = archive.getSteps();
					int rows = archive.getRows();
					rrdDef.addArchive(type, 0.5, steps, rows);
				}
				rrdDb = pool.requestRrdDb(rrdDef);
			}
		}
		catch (Exception e)
		{

		}
		finally
		{
			try
			{
				pool.release(rrdDb);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void writeData(String rrd, String ds, Double value)
	{
		RrdDbPool pool = null;
		RrdDb rrdDb = null;
		try
		{
			pool = RrdDbPool.getInstance();
			long time = Util.normalize(Util.getTime(), step);
			File f = new File(rrdDir, rrd);
			rrdDb = pool.requestRrdDb(f.getPath());
			Sample sample = rrdDb.createSample(time);
			if (value == null)
				value = Double.NaN;
			sample.setValue(ds, value);
			sample.update();
		}
		catch (Exception e)
		{

		}
		finally
		{
			try
			{
				pool.release(rrdDb);
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		}
	}

	public double[] fetch(String rrd, String ds, int size, int steps)
	{
		File file = new File(rrdDir, rrd);
		RrdDb rrdDb = null;
		double[] vs = null;
		try
		{
			rrdDb = RrdDbPool.getInstance().requestRrdDb(file.getPath());
			long e = rrdDb.getLastUpdateTime();
			org.jrobin.core.Archive archive = rrdDb.getArchive("AVERAGE", steps);
			long archStart = archive.getStartTime() - archive.getArcStep();
			long archEnd = archive.getEndTime();
			long s = e - (size - 1) * step * steps;
			if (s < archStart) {
				s = archStart;
			}
			if (e > archEnd) {
				e = archEnd;
			}
			FetchRequest f = rrdDb.createFetchRequest("AVERAGE", s, e, step * steps);
			FetchData dt = f.fetchData();
			vs = dt.getValues(ds);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return vs;
	}
}
