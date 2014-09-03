package org.rzy.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ognl.Ognl;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.rzy.util.Pager;

@SuppressWarnings("unchecked")
public final class SQLMapper
{
	private static Dao dao = Dao.getInstance();
	private static Map<String, String> sqls = new HashMap<String, String>();
	static
	{
		InputStream is = null;
		try
		{
			URL url = SQLMapper.class.getClassLoader().getResource("mappers");
			File f = new File(url.toURI());
			File[] xmls = f.listFiles(new FilenameFilter()
			{
				public boolean accept(File dir, String name)
				{
					return name.endsWith("xml");
				}
			});
			for (File file : xmls)
			{
				String fn = file.getName();
				String path = "mappers/" + fn;
				String ns = fn.substring(0, fn.lastIndexOf('.'));
				is = SQLMapper.class.getClassLoader().getResourceAsStream(path);
				if (is == null)
				{
					is = new FileInputStream(path);
				}
				SAXReader reader = new SAXReader();
				Document doc = reader.read(is);
				Element root = doc.getRootElement();
				List<Element> list = root.elements();
				for (Element e : list)
				{
					String nodeName = e.getName();
					String sqlid = e.attributeValue("id");
					String outerXML = e.asXML();
					String innerXML = outerXML.replaceAll("^<" + nodeName + ".*?>|</" + nodeName + ">$", "");
					sqls.put(ns + "." + sqlid, innerXML);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Load sql failed", e);
		}
	}

	private SQLMapper()
	{

	}

	public static void update(String sqlid, Map<String, ?> params)
	{
		SQL s = getSQL(sqlid, params);
		String sql = s.getSql();
		List<Object> ps = s.getPs();
		dao.begin();
		dao.update(sql, ps.toArray());
		dao.commit();
	}

	public static Map<String, Object> findOne(String sqlid, Map<String, ?> params)
	{
		SQL s = getSQL(sqlid, params);
		String sql = s.getSql();
		List<Object> ps = s.getPs();
		List<Map<String, Object>> list = dao.find(sql, ps.toArray());
		if (list != null && list.size() > 0)
		{
			return list.get(0);
		}
		return null;
	}

	public static List<Map<String, Object>> find(String sqlid, Map<String, ?> params)
	{
		SQL s = getSQL(sqlid, params);
		String sql = s.getSql();
		List<Object> ps = s.getPs();
		return dao.find(sql, ps.toArray());
	}

	public static Pager pager(String countsqlid, String pagersqlid, Map<String, ?> params)
	{
		int page = org.apache.commons.lang.math.NumberUtils.toInt(params.get("page").toString(), 1);
		int pagesize = org.apache.commons.lang.math.NumberUtils.toInt(params.get("pagesize").toString(), 10);
		Pager pager = new Pager(page, pagesize);
		SQL s1 = getSQL(countsqlid, params);
		SQL s2 = getSQL(pagersqlid, params);
		String sql1 = s1.getSql();
		String sql2 = s2.getSql();
		;
		int total = 0;
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		Object scalar = dao.scalar(sql1, s1.getPs().toArray());
		if (scalar != null)
		{
			total = Integer.valueOf(scalar.toString());
			pager.setTotal(total);
		}
		if (total > 0)
		{
			int pagecount = (total % pagesize == 0) ? (total / pagesize) : (total / pagesize + 1);
			if (pagecount > 0)
			{
				page = (pagecount < page) ? pagecount : page;
			}
			data = dao.pager(sql2, s2.getPs().toArray(), page, pagesize);
			pager.setData(data);
		}
		return pager;
	}

	private static SQL parse(String text, String openToken, String closeToken, Map<String, ?> params)
	{
		StringBuilder builder = new StringBuilder();
		List<Object> ps = new ArrayList<Object>();
		if (text != null && text.length() > 0)
		{// 如果传入的字符串有值
			// 将字符串转为字符数组
			char[] src = text.toCharArray();
			int offset = 0;
			// 判断openToken在text中的位置，注意indexOf函数的返回值-1表示不存在，0表示在在开头的位置
			int start = text.indexOf(openToken, offset);
			while (start > -1)
			{
				if (start > 0 && src[start - 1] == '\\')
				{
					// 如果text中在openToken前存在转义符就将转义符去掉。如果openToken前存在转义符，start的值必然大于0，最小也为1
					// 因为此时openToken是不需要进行处理的，所以也不需要处理endToken。接着查找下一个openToken
					builder.append(src, offset, start - 1).append(openToken);
					offset = start + openToken.length();// 重设offset
				}
				else
				{
					int end = text.indexOf(closeToken, start);
					if (end == -1)
					{// 如果不存在openToken，则直接将offset位置后的字符添加到builder中
						builder.append(src, offset, src.length - offset);
						offset = src.length;// 重设offset
					}
					else
					{
						builder.append(src, offset, start - offset);// 添加openToken前offset后位置的字符到bulider中
						offset = start + openToken.length();// 重设offset
						String content = new String(src, offset, end - offset);// 获取openToken和endToken位置间的字符串
						// builder.append(handler.handleToken(content));//调用handler进行处理
						ps.add(params.get(content));
						builder.append("?");// 调用handler进行处理
						offset = end + closeToken.length();// 重设offset
					}
				}
				start = text.indexOf(openToken, offset);// 开始下一个循环
			}
			// 只有当text中不存在openToken且text.length大于0时才会执行下面的语句
			if (offset < src.length)
			{
				builder.append(src, offset, src.length - offset);
			}
		}
		SQL s = new SQL();
		s.setSql(builder.toString());
		s.setPs(ps);
		return s;
	}

	public static SQL getSQL(String sqlid, Map<String, ?> params)
	{
		SQL tt = null;
		String sqlxml = sqls.get(sqlid);
		String regEx = "\\w*(<where>[\\s\\S]*?</where>)\\w*";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(sqlxml);
		String wherestr = "";
		String wherexml = null;
		if (mat.find())
		{
			wherexml = mat.group(1);
		}
		if (StringUtils.isNotBlank(wherexml))
		{
			SAXReader saxReader = new SAXReader();
			Document doc = null;

			try
			{
				doc = saxReader.read(new StringReader(wherexml));
				Node where = doc.selectSingleNode("where");
				StringBuilder sb = new StringBuilder();
				if (where != null)
				{
					List<Element> ifs = where.selectNodes("if");
					for (Element e : ifs)
					{
						String exp = e.attributeValue("test");
						boolean test = (Boolean) Ognl.getValue(exp, params);
						if (test)
						{
							String text = e.getTextTrim();
							sb.append(" ");
							sb.append(text);
						}
					}
				}
				wherestr = sb.toString().replaceFirst("and", "where").trim();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		String sql = sqlxml.replaceAll("<where>[\\s\\S]*?</where>", wherestr);
		sql = sql.replaceAll("\n\t*", " ").replaceAll("\\s{2,}", " ").trim();
		tt = parse(sql, "#{", "}", params);
		return tt;
	}

	public static void main(String[] args)
	{
		String sqlid = "log.selectAll";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("operator", "admin");
		params.put("operator_text", "admin");
		params.put("time1", "2014-08-28");
		params.put("time2", "2014-08-29");
		List<Map<String, Object>> list = SQLMapper.find(sqlid, params);
		System.out.println(list.size());
	}
}