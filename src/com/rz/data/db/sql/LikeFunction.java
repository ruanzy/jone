package com.rz.data.db.sql;

import org.beetl.core.Context;
import org.beetl.core.Function;

public class LikeFunction implements Function {

	public Object call(Object[] paras, Context ctx) {
		String p0 = null;
		String p1 = null;
		Object _p0 = paras[0];
		Object _p1 = paras[0];
		if (_p0 instanceof String) {
			p0 = (String) paras[0];
		} else {
			throw new RuntimeException("like.paras[0] must be a String!");
		}
		if (_p1 instanceof String) {
			p1 = (String) paras[1];
		} else {
			throw new RuntimeException("like.paras[1] must be 'l'/'m'/'r'!");
		}
		StringBuffer like = new StringBuffer();
		like.append("like ");
		if ("l".equals(p1)) {
			like.append("'").append(p0).append("%'");
		} else if ("r".equals(p1)) {
			like.append("'%").append(p0).append("'");
		} else if ("m".equals(p1)) {
			like.append("'%").append(p0).append("%'");
		}
		try {
			ctx.byteWriter.writeString(like.toString());
		} catch (Exception e) {
		}
		return null;
	}
}
