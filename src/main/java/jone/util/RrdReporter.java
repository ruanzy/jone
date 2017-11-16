package jone.util;

import static org.rrd4j.ConsolFun.AVERAGE;
import static org.rrd4j.DsType.GAUGE;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.Clock;
import com.codahale.metrics.Counter;
import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Timer;

/**
 * A reporter which creates a comma-separated values file of the measurements for each metric.
 */
public class RrdReporter extends ScheduledReporter {
    /**
     * Returns a new {@link Builder} for {@link CsvReporter}.
     *
     * @param registry the registry to report
     * @return a {@link Builder} instance for a {@link CsvReporter}
     */
    public static Builder forRegistry(MetricRegistry registry) {
        return new Builder(registry);
    }

    /**
     * A builder for {@link CsvReporter} instances. Defaults to using the default locale, converting
     * rates to events/second, converting durations to milliseconds, and not filtering metrics.
     */
    public static class Builder {
        private final MetricRegistry registry;
        private Locale locale;
        private TimeUnit rateUnit;
        private TimeUnit durationUnit;
        private Clock clock;
        private MetricFilter filter;

        private Builder(MetricRegistry registry) {
            this.registry = registry;
            this.locale = Locale.getDefault();
            this.rateUnit = TimeUnit.SECONDS;
            this.durationUnit = TimeUnit.MILLISECONDS;
            this.clock = Clock.defaultClock();
            this.filter = MetricFilter.ALL;
        }

        /**
         * Format numbers for the given {@link Locale}.
         *
         * @param locale a {@link Locale}
         * @return {@code this}
         */
        public Builder formatFor(Locale locale) {
            this.locale = locale;
            return this;
        }

        /**
         * Convert rates to the given time unit.
         *
         * @param rateUnit a unit of time
         * @return {@code this}
         */
        public Builder convertRatesTo(TimeUnit rateUnit) {
            this.rateUnit = rateUnit;
            return this;
        }

        /**
         * Convert durations to the given time unit.
         *
         * @param durationUnit a unit of time
         * @return {@code this}
         */
        public Builder convertDurationsTo(TimeUnit durationUnit) {
            this.durationUnit = durationUnit;
            return this;
        }

        /**
         * Use the given {@link Clock} instance for the time.
         *
         * @param clock a {@link Clock} instance
         * @return {@code this}
         */
        public Builder withClock(Clock clock) {
            this.clock = clock;
            return this;
        }

        /**
         * Only report metrics which match the given filter.
         *
         * @param filter a {@link MetricFilter}
         * @return {@code this}
         */
        public Builder filter(MetricFilter filter) {
            this.filter = filter;
            return this;
        }

        /**
         * Builds a {@link CsvReporter} with the given properties, writing {@code .csv} files to the
         * given directory.
         *
         * @param directory the directory in which the {@code .csv} files will be created
         * @return a {@link CsvReporter}
         */
        public RrdReporter build(File directory, int step) {
            return new RrdReporter(registry,
                                   directory,
                                   locale,
                                   rateUnit,
                                   durationUnit,
                                   clock,
                                   step,
                                   filter);
        }
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvReporter.class);
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final File directory;
    private final Locale locale;
    private final Clock clock;
    private final int step;

    private RrdReporter(MetricRegistry registry,
                        File directory,
                        Locale locale,
                        TimeUnit rateUnit,
                        TimeUnit durationUnit,
                        Clock clock,
                        int step,
                        MetricFilter filter) {
        super(registry, "rrd-reporter", filter, rateUnit, durationUnit);
        this.directory = directory;
        this.locale = locale;
        this.clock = clock;
        this.step = step;
    }

    @Override
    public void report(SortedMap<String, Gauge> gauges,
                       SortedMap<String, Counter> counters,
                       SortedMap<String, Histogram> histograms,
                       SortedMap<String, Meter> meters,
                       SortedMap<String, Timer> timers) {
        final long timestamp = TimeUnit.MILLISECONDS.toSeconds(clock.getTime());

        for (Map.Entry<String, Gauge> entry : gauges.entrySet()) {
            reportGauge(timestamp, entry.getKey(), entry.getValue());
        }
    }

    private void reportGauge(long timestamp, String name, Gauge gauge) {
        report(timestamp, name, gauge.getValue());
    }

    private void report(long timestamp, String ds, Object value) {
        try {
            final File file = new File(directory, sanitize(ds) + ".rrd");
            final boolean fileAlreadyExists = file.exists();
            if (fileAlreadyExists) {
            	writeRrd(file.getPath(), ds, timestamp, Double.parseDouble(value.toString()));
            }else{
            	createRrd(file.getPath(), ds);
            }
        } catch (Exception e) {
            LOGGER.warn("Error writing to {}", ds, e);
        }
    }

    protected String sanitize(String name) {
        return name;
    }
    
	public void createRrd(String filePath, String ds) {
		int STEP = step;
		int YEAR = 60 * 60 * 24 * 365;
		int DAY = 60 * 60 * 24;
		int HOUR = 60 * 60;
		int MINUTE = 60;
		long START = Util.getTimestamp();
		RrdDb rrdDb = null;
		try {
			RrdDef rrdDef = new RrdDef(filePath, START - 1, STEP);
			rrdDef.addDatasource(ds, GAUGE, 2 * STEP, 0, Double.NaN);
			rrdDef.addArchive(AVERAGE, 0.5, 1, YEAR / STEP);
			rrdDef.addArchive(AVERAGE, 0.5, DAY / STEP, YEAR / DAY);
			rrdDef.addArchive(AVERAGE, 0.5, HOUR / STEP, YEAR / HOUR);
			rrdDef.addArchive(AVERAGE, 0.5, MINUTE / STEP, YEAR / MINUTE);
			rrdDb = new RrdDb(rrdDef);
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
	
	public void writeRrd(String filePath, String ds, long timestamp, double v) {
		RrdDb rrdDb = null;
		try {
			rrdDb = new RrdDb(filePath);
			Sample sample = rrdDb.createSample();
			long time = Util.normalize(timestamp, step);
			long lastUpdateTime = rrdDb.getLastUpdateTime();
			if(time > lastUpdateTime){
				sample.setTime(time);
				sample.setValue(ds, v);
				sample.update();
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
}