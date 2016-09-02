package com.rz.common;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.digest.DigestUtils;

public class TokenUtil {
	static final long DIFF = 60 * 60 * 2;
	static Map<String, Date> tokenStore = new ConcurrentHashMap<String, Date>();
	static ScheduledExecutorService service = Executors
			.newScheduledThreadPool(10);
	static {
		service.scheduleAtFixedRate(new Runnable() {
			public void run() {
				long t1 = new Date().getTime();
				for (Map.Entry<String, Date> entry : tokenStore.entrySet()) {
					long t2 = entry.getValue().getTime();
					if (t2 - t1 > DIFF) {
						tokenStore.remove(entry.getKey());
					}
				}
			}
		}, DIFF, DIFF, TimeUnit.SECONDS);
	}

	public static String generatorToken(String signature) {
		Date time = new Date();
		try {
			byte[] b = (time + DigestUtils.md5Hex(signature)).getBytes("UTF-8");
			String token = DigestUtils.md5Hex(b);
			tokenStore.put(token, time);
			return token;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return signature;
	}

	public static void removeToken(String token) {
		if (token == null)
			return;
		if (tokenStore.containsKey(token)) {
			tokenStore.remove(token);
		}
	}

	public static boolean validateToken(String token) {
		if (token == null)
			return false;
		if (tokenStore.containsKey(token)) {
			Date time = tokenStore.get(token);
			Date normal = new Date();
			if (normal.getTime() - time.getTime() > DIFF * 1000) {
				tokenStore.remove(token);
				return false;
			}else{
				tokenStore.put(token, normal);
			}
		} else {
			return false;
		}
		return true;
	}
}
