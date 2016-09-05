package com.rz.common;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	public static String encode(String str) {
		String s = null;
		try {
			s = new String(Base64.encodeBase64(str.getBytes()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static String decode(String str) {
		String result = null;
		try {
			result = new String(Base64.decodeBase64(str.getBytes()), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
