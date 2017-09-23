package jone.util;

import static org.rrd4j.ConsolFun.AVERAGE;
import static org.rrd4j.DsType.GAUGE;

import java.io.File;
import java.io.IOException;

import jone.R;

import org.rrd4j.ConsolFun;
import org.rrd4j.core.Archive;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;

public class RrdUtils {
	static final String RRDDIR = "e:/myrrd";
	static final long START = Util.getTimestamp();
	public static final int STEP = 5;
	static final int YEAR = 60 * 60 * 24 * 365;
	static final int DAY = 60 * 60 * 24;
	static final int HOUR = 60 * 60;
	static final int MINUTE = 60;

	public static void createRrd(String fileName, String ds) {
		String rrdPath = new File(RRDDIR, fileName).getPath();
		RrdDef rrdDef = new RrdDef(rrdPath, START - 1, STEP);
		rrdDef.addDatasource(ds, GAUGE, 2 * STEP, 0, Double.NaN);
		rrdDef.addArchive(AVERAGE, 0.5, 1, YEAR / STEP);
		rrdDef.addArchive(AVERAGE, 0.5, DAY / STEP, YEAR / DAY);
		rrdDef.addArchive(AVERAGE, 0.5, HOUR / STEP, YEAR / HOUR);
		rrdDef.addArchive(AVERAGE, 0.5, MINUTE / STEP, YEAR / MINUTE);
		RrdDb rrdDb = null;
		try {
			rrdDb = new RrdDb(rrdDef);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				rrdDb.close();
			} catch (IOException e) {
				e.printStackTrace();
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
			System.out.println(time + "====" + v);
		} catch (Exception e) {
			e.printStackTrace();
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
		}
		return null;
	}

	public static R readLastValues(String fileName, String ds, int size, int steps) {
		R r = new R();
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
			double[] values = fetchData.getValues(ds);
			long[] timestamps = fetchData.getTimestamps();
			r.put("timestamps", timestamps);
			r.put("values", values);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}