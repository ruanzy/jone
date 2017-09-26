package jone.util;

import static org.rrd4j.ConsolFun.AVERAGE;
import static org.rrd4j.DsType.GAUGE;

import java.io.File;
import java.io.IOException;

import org.rrd4j.ConsolFun;
import org.rrd4j.core.Archive;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;

public class RrdUtils {
	static final String RRDDIR = Cfg.getString("rrdDir");
	static final long START = Util.getTimestamp();
	public static final int STEP = 5;
	static final int YEAR = 60 * 60 * 24 * 365;
	static final int DAY = 60 * 60 * 24;
	static final int HOUR = 60 * 60;
	static final int MINUTE = 60;

	public static void createRrd(String fileName, String ds) {
		RrdDb rrdDb = null;
		try {
			File f = new File(RRDDIR, fileName);
			if(!f.exists()){
				String rrdPath = f.getPath();
				RrdDef rrdDef = new RrdDef(rrdPath, START - 1, STEP);
				rrdDef.addDatasource(ds, GAUGE, 2 * STEP, 0, Double.NaN);
				rrdDef.addArchive(AVERAGE, 0.5, 1, YEAR / STEP);
				rrdDef.addArchive(AVERAGE, 0.5, DAY / STEP, YEAR / DAY);
				rrdDef.addArchive(AVERAGE, 0.5, HOUR / STEP, YEAR / HOUR);
				rrdDef.addArchive(AVERAGE, 0.5, MINUTE / STEP, YEAR / MINUTE);
				rrdDb = new RrdDb(rrdDef);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rrdDb != null){
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
	
	public static void writeRrd(String fileName, String ds, double v) {
		RrdDb rrdDb = null;
		try {
			String rrdPath = new File(RRDDIR, fileName).getPath();
			rrdDb = new RrdDb(rrdPath);
			Sample sample = rrdDb.createSample();
			long time = Util.normalize(Util.getTimestamp(), STEP);
			sample.setTime(time);
			sample.setValue(ds, v);
			sample.update();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rrdDb != null){
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

	public static double[] readRrd(String fileName, String ds, long start, long end) {
		RrdDb rrdDb = null;
		try {
			String rrdPath = new File(RRDDIR, fileName).getPath();
			rrdDb = new RrdDb(rrdPath);
			FetchRequest request = rrdDb.createFetchRequest(AVERAGE, start, end);
			System.out.println(request.dump());
			FetchData fetchData = request.fetchData();
			return fetchData.getValues(ds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rrdDb != null){
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
		return null;
	}
	
	public static double[] readLastValues(String fileName, String ds, int size, int steps) {
		RrdDb rrdDb = null;
		try {
			long resolution = steps * RrdUtils.STEP;
			String rrdPath = new File(RRDDIR, fileName).getPath();
			rrdDb = new RrdDb(rrdPath);
			Archive archive = rrdDb.getArchive(ConsolFun.AVERAGE, steps);
			long end = archive.getEndTime();
			long start = end - (size - 1) * resolution;
			FetchRequest request = rrdDb.createFetchRequest(AVERAGE, start, end, resolution);
			FetchData fetchData = request.fetchData();
			return fetchData.getValues(ds);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rrdDb != null){
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
		return null;
	}
}