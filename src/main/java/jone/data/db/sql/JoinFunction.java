package jone.data.db.sql;

import java.util.Collection;
import java.util.List;

import org.beetl.core.Context;
import org.beetl.core.Function;

public class JoinFunction implements Function {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object call(Object[] paras, Context ctx) {
		Object[] objs = null;
		Object temp = paras[0];
		if (temp instanceof Object[]) {
			objs = (Object[]) paras[0];
		} else if (temp instanceof Collection) {
			objs = ((Collection) temp).toArray();
		} else {
			throw new RuntimeException(
					"join.paras[0] must be a collection or an array!");
		}

		List<Object> dbParas = (List<Object>) ctx.getGlobal("_paras");

		try {
			ctx.byteWriter.writeString(join(objs, dbParas));
		} catch (Exception e) {
		}
		return null;
	}

	private static String join(Object[] array, List<Object> dbParas) {
		if (array == null) {
			return null;
		}
		int arraySize = array.length;
		StringBuffer buf = new StringBuffer(arraySize * 2 - 1);

		for (int i = 0; i < arraySize; i++) {
			if (i > 0) {
				buf.append(',');
			}
			if (array[i] instanceof String) {
				buf.append("'").append(array[i]).append("'");
			} else {

				buf.append(array[i]);
			}
			// dbParas.add(array[i]);
		}
		return buf.toString();
	}
}
