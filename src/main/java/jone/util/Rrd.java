package jone.util;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.ArcDef;
import org.rrd4j.core.DsDef;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Util;

public class Rrd {

	private String path = "";
	private long startTime = 0;
	private long step = 0;
	private RrdDef rrdDef = null;
	private RrdDb rrdDb = null;

	public Rrd(String path, long step) {
		this.path = path;
		this.startTime = Util.getTimestamp();
		this.step = step;
		this.rrdDb = null;
		this.rrdDef = new RrdDef(this.path, this.startTime - 1, this.step);
		this.rrdDef.setVersion(2);
	}

	public Rrd addDataSource(String dsName, DsType dsType) {
		if (this.rrdDef == null || dsName == null || dsName.equals("")) {
			return null;
		}
		this.rrdDef.addDatasource(dsName, dsType, 2 * this.step, 0, Double.NaN);
		return this;
	}

	public Rrd addDataSource(DsDef dsDef) {
		if (this.rrdDef == null || dsDef == null) {
			return null;
		}
		this.rrdDef.addDatasource(dsDef);
		return this;
	}

	public Rrd addDataSources(List<DsDef> dsDefList) {
		if (this.rrdDef == null || dsDefList == null) {
			return null;
		}
		for (DsDef dsDef : dsDefList) {
			this.rrdDef.addDatasource(dsDef);
		}
		return this;
	}

	public Rrd addArchive(ConsolFun consolFun, int steps, int rows) {
		this.rrdDef.addArchive(consolFun, 0.5, steps, rows);
		return this;
	}

	public Rrd addArchive(ArcDef arcDef) {
		if (this.rrdDef == null || arcDef == null) {
			return null;
		}
		this.rrdDef.addArchive(arcDef);
		return this;
	}

	public Rrd addArchives(List<ArcDef> arcDefsList) {
		if (this.rrdDef == null || arcDefsList == null) {
			return null;
		}
		for (ArcDef arcDef : arcDefsList) {
			this.rrdDef.addArchive(arcDef);
		}
		return this;
	}

	public Rrd build() {
		try {
			this.rrdDb = new RrdDb(this.rrdDef);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return this;
	}

	public void close() {
		try {
			this.rrdDb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean update(String dsName, double value) {
		try {
			long time = Util.normalize(Util.getTimestamp(), this.step);//这里时间要进行调整，否则数据不对
			this.rrdDb.createSample(time).setValue(dsName, value).update();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public FetchData fetch(ConsolFun fun, long start, long end) {
		FetchData fetchData;
		FetchRequest request = this.rrdDb.createFetchRequest(fun, start, end);
		try {
			fetchData = request.fetchData();
			System.out.println("== Data fetched. " + fetchData.getRowCount() + " points obtained");
			System.out.println(fetchData.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return fetchData;
	}

	public FetchData fetch(ConsolFun fun, long start, long end, long resolution) {
		FetchData fetchData;
		FetchRequest request = this.rrdDb.createFetchRequest(fun, start, end, resolution);
		try {
			fetchData = request.fetchData();
			System.out.println("== Data fetched. " + fetchData.getRowCount() + " points obtained");
			System.out.println(fetchData.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return fetchData;
	}
	
	public static void main(String[] args) {
		final Random random = new Random();
		final Rrd rrd = new Rrd("e:/demo234.rrd", 5);
		rrd.addDataSource("a", DsType.GAUGE).addArchive(ConsolFun.AVERAGE, 1, 40).addArchive(ConsolFun.AVERAGE, 6, 20)
				.build();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				rrd.update("a", random.nextInt(10));
			}
		}, 1000, 5000);

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				long end = System.currentTimeMillis() / 1000;
				long s = end - 25;
				FetchData fetch = rrd.fetch(ConsolFun.AVERAGE, s, end);
				System.out.println(fetch.getValues().toString());
			}
		}, 1000, 5000);
	}
}
