package jone.data.db.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import jone.template.Engine;
import jone.template.Template;

public class SQLLoader {
	static String lineSeparator = System.getProperty("line.separator", "\n");
	public static Map<String, String> sqls = new LinkedHashMap<String, String>();
	static Engine engine;
	static {
		try {
			File f = new File("d:/tpl");
			engine = Engine.use();
			engine.addDirective("p", new ParaDirective());
			engine.addDirective("in", new InDirective());
			engine.setBaseTemplatePath(f.getPath());
			File[] sf = f.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.endsWith("sql");
				}
			});
			for (File file : sf) {
				loadSql(file);
			}
		} catch (Exception e) {
			throw new RuntimeException("Load sql failed", e);
		}
	}

	public static SqlPara getSql(String sqlid, Map<String, Object> params) {
		String sqlTmpl = sqls.get(sqlid);
		if(null == sqlTmpl){
			String msg = String.format("Failed to obtain SQL through [%s]", sqlid);
			throw new RuntimeException(msg);
		}
		Template t = engine.getTemplateByString(sqlTmpl);
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

	private static void loadSql(File file) {
		String fn = file.getName();
		String ns = fn.substring(0, fn.lastIndexOf('.'));
		LinkedList<String> list = new LinkedList<String>();
		BufferedReader bf = null;
		try {
			FileInputStream ins = new FileInputStream(file);
			bf = new BufferedReader(new InputStreamReader(ins));
			String temp = null;
			StringBuilder sql = null;
			String key = null;
			int lineNum = 0;
			// int findLineNum = 0;
			while ((temp = bf.readLine()) != null) {
				temp = temp.trim();
				lineNum = lineNum + 1;
				if (temp.startsWith("===")) {// 读取到===号，说明上一行是key，下面是注释或者SQL语句
					if (!list.isEmpty() && list.size() > 1) {// 如果链表里面有多个，说明是上一句的sql+下一句的key
						String tempKey = list.pollLast();// 取出下一句sql的key先存着
						sql = new StringBuilder();
						key = list.pollFirst();
						while (!list.isEmpty()) {// 拼装成一句sql
							sql.append(list.pollFirst() + lineSeparator);
						}
						sqls.put(ns + "." + key, sql.toString().trim());
						list.addLast(tempKey);// 把下一句的key又放进来
						// findLineNum = lineNum;
					}
					boolean sqlStart = false;
					String tempNext = null;
					while ((tempNext = bf.readLine()) != null) {// 处理注释的情况
						tempNext = tempNext.trim();
						lineNum++;
						if (tempNext.startsWith("*")) {// 读到注释行，不做任何处理
							continue;
						} else if (!sqlStart && tempNext.trim().length() == 0) {
							// 注释的空格
							continue;
						} else {
							sqlStart = true;
							list.addLast(tempNext);// ===下面不是*号的情况，是一条sql
							break;// 读到一句sql就跳出循环
						}
					}
				} else {
					list.addLast(temp);
				}
			}
			// 最后一句sql
			sql = new StringBuilder();
			key = list.pollFirst();
			while (!list.isEmpty()) {
				sql.append(list.pollFirst() + lineSeparator);
			}
			sqls.put(ns + "." + key, sql.toString().trim());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
