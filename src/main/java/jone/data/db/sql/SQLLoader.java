package jone.data.db.sql;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import jone.template.Engine;
import jone.template.Template;

public class SQLLoader {
	public static Map<String, Template> sqlTemplateMap = new LinkedHashMap<String, Template>();
	static Engine engine;
	static {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("sql");
			File f = new File(url.toURI());
			engine = Engine.use();
			engine.addDirective("sql", new SqlDirective());
			engine.addDirective("p", new ParaDirective());
			engine.addDirective("in", new InDirective());
			engine.setBaseTemplatePath(f.getPath());
			File[] sf = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith("sql");
				}
			});
			for (File file : sf) {
				String fn = file.getName();
				String ns = fn.substring(0, fn.lastIndexOf('.'));
				Template t = engine.getTemplate(file.getName());
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("NAMESPACE_KEY", ns);
				t.renderToString(data);
			}
		} catch (Exception e) {
			throw new RuntimeException("Load sql failed", e);
		}
	}

	public static SqlPara getSql(String sqlid, Map<String, Object> params) {
		Template t = sqlTemplateMap.get(sqlid);
		if(null == t){
			String msg = String.format("Failed to obtain SQL through [%s]", sqlid);
			throw new RuntimeException(msg);
		}
		SqlPara sql = new SqlPara();
		params.put("_SQL_PARA_", sql);
		String sqltext = t.renderToString(params);
		params.remove("_SQL_PARA_");
		if (true) {
			sqltext = sqltext.replaceAll("\n\t*", " ")
					.replaceAll("\\s{2,}", " ").trim()
					.replaceAll("where 1=1 and", "where")
					.replaceAll("where 1=1", "");
		}
		sql.setSql(sqltext);
		return sql;
	}
}
