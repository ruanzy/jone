package com.rz.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rz.common.Scaner;

public class Container {
	private static Logger log = LoggerFactory.getLogger(Container.class);
	private static Properties props = new Properties();
	private static Map<String, Object> actions = new HashMap<String, Object>();
	private static Map<String, Interceptor> interceptors = new HashMap<String, Interceptor>();

	public static void init() {
		readCfg();
		loadActions();
		loadInterceptors();
	}

	private static void readCfg() {
		InputStream is = null;
		try {
			String fileName = "jone.properties";
			is = Thread.currentThread().getContextClassLoader()
					.getResourceAsStream(fileName);
			if (is != null) {
				log.debug("Loading jone.properties");
				props.load(is);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {

			}
		}
	}

	private static String getActionsPck() {
		return props.getProperty("action.package", "action");
	}

	private static String getInterceptorsPck() {
		return props.getProperty("interceptor.package", "interceptor");
	}

	private static void loadActions() {
		String pck = getActionsPck();
		log.debug("Loading actions in " + pck);
		try {
			Set<Class<?>> _actions = Scaner.scan(pck);
			for (Class<?> cls : _actions) {
				String name = cls.getName();
				actions.put(name, cls.newInstance());
				log.debug("Loading action " + name);
			}
			log.debug("Actions loaded!");
		} catch (Exception e) {
			throw new RuntimeException("Actions load error!", e);
		}
	}

	private static void loadInterceptors() {
		String pck = getInterceptorsPck();
		log.debug("Loading interceptors in " + pck + " package");
		try {
			Set<Class<?>> _actions = Scaner.scan(pck);
			for (Class<?> cls : _actions) {
				String name = cls.getName();
				Expression expression = cls.getAnnotation(Expression.class);
				if (expression != null) {
					String exp = expression.value();
					interceptors.put(exp, (Interceptor) cls.newInstance());
				}
				log.debug("Loading interceptor " + name);
			}
			log.debug("Interceptors loaded!");
		} catch (Exception e) {
			throw new RuntimeException("Interceptors load error!", e);
		}
	}

	public static Object findAction(String actionName) {
		String pck = getActionsPck();
		return actions.get(pck + "." + actionName);
	}

	public static List<Interceptor> findInterceptor(String action, String method) {
		List<Interceptor> list = new ArrayList<Interceptor>();
		String express1 = action;
		String express2 = action + "/" + method;
		Set<String> keys = interceptors.keySet();
		for (String key : keys) {
			if (key.equals("*") || key.equals(express1) || key.equals(express2)) {
				list.add(interceptors.get(key));
			}
		}
		return list;
	}
}
