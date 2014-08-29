package org.rzy.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class DynamicSQL
{
	static Map<String, String> sqls = new HashMap<String, String>();
	static
	{
		InputStream is = null;
		try
		{
			URL url = DynamicSQL.class.getClassLoader().getResource("sqls");
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
				String path = "sqls/" + fn;
				String ns = fn.substring(0, fn.lastIndexOf('.'));
				is = DynamicSQL.class.getClassLoader().getResourceAsStream(path);
				if (is == null)
				{
					is = new FileInputStream(path);
				}
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(is);
				Element root = doc.getDocumentElement();
				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPath xpath = xpathFactory.newXPath();
				NodeList result = (NodeList) xpath.evaluate("/sqls/sql", root, XPathConstants.NODESET);
				for (int i = 0; i < result.getLength(); i++)
				{
					Node n = result.item(i);
					String sqlid = n.getAttributes().getNamedItem("id").getNodeValue();
					String sql = n.getTextContent().replaceAll("\\s+|\t|\r|\n", " ").trim();
					
					sqls.put(ns + "." + sqlid, sql);
				}
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Load sql failed", e);
		}
	}
	
	public static SQL getSQL(String sqlid, Map<String, ?> params)
	{
		try
		{
			Configuration cfg = new Configuration();
			StringTemplateLoader sTmpLoader = new StringTemplateLoader();
			sTmpLoader.putTemplate(sqlid, sqls.get(sqlid));
			cfg.setTemplateLoader(sTmpLoader);
			cfg.setDefaultEncoding("UTF-8");
			Template template = cfg.getTemplate(sqlid);
			StringWriter writer = new StringWriter();
			template.process(params, writer);
			SQL s = parse(writer.toString(), ":{", "}", params) ;
			System.out.println(s.getSql());
			System.out.println(s.getPs());
			return s;
		}
		catch (Exception e)
		{
			throw new RuntimeException("Parse sql failed", e);
		}
	}

	public static String getSQL2(String sqlid, Map<String, ?> params)
	{
		try
		{
			Configuration cfg = new Configuration();
			StringTemplateLoader sTmpLoader = new StringTemplateLoader();
			sTmpLoader.putTemplate(sqlid, sqls.get(sqlid));
			cfg.setTemplateLoader(sTmpLoader);
			cfg.setDefaultEncoding("UTF-8");
			Template template = cfg.getTemplate(sqlid);
			StringWriter writer = new StringWriter();
			template.process(params, writer);
			System.out.println(writer.toString());
			return writer.toString();
		}
		catch (Exception e)
		{
			throw new RuntimeException("Parse sql failed", e);
		}
	}
	
	public static SQL parse(String text, String openToken, String closeToken, Map<String, ?> params) {
	    StringBuilder builder = new StringBuilder();
	    List<Object> ps = new ArrayList<Object>();
	    if (text != null && text.length() > 0) {//如果传入的字符串有值
	      //将字符串转为字符数组
	      char[] src = text.toCharArray();
	      int offset = 0;
	      //判断openToken在text中的位置，注意indexOf函数的返回值-1表示不存在，0表示在在开头的位置
	      int start = text.indexOf(openToken, offset);
	      while (start > -1) {
	        if (start > 0 && src[start - 1] == '\\') {
	          //如果text中在openToken前存在转义符就将转义符去掉。如果openToken前存在转义符，start的值必然大于0，最小也为1
	          //因为此时openToken是不需要进行处理的，所以也不需要处理endToken。接着查找下一个openToken
	          builder.append(src, offset, start - 1).append(openToken);
	          offset = start + openToken.length();//重设offset
	        } else {
	          int end = text.indexOf(closeToken, start);
	          if (end == -1) {//如果不存在openToken，则直接将offset位置后的字符添加到builder中
	            builder.append(src, offset, src.length - offset);
	            offset = src.length;//重设offset
	          } else {
	            builder.append(src, offset, start - offset);//添加openToken前offset后位置的字符到bulider中
	            offset = start + openToken.length();//重设offset
	            String content = new String(src, offset, end - offset);//获取openToken和endToken位置间的字符串
	            //builder.append(handler.handleToken(content));//调用handler进行处理
	            ps.add(params.get(content));
	            builder.append("?");//调用handler进行处理
	            offset = end + closeToken.length();//重设offset
	          }
	        }
	        start = text.indexOf(openToken, offset);//开始下一个循环
	      }
	      //只有当text中不存在openToken且text.length大于0时才会执行下面的语句
	      if (offset < src.length) {
	        builder.append(src, offset, src.length - offset);
	      }
	    }
	    SQL s = new SQL();
	    s.setSql(builder.toString());
	    s.setPs(ps);
	    return s;
	  }
	
	
	public static void main(String[] args)
	{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("operator", "admin");
		param.put("operator_text", "admin");
		param.put("time1", "2014-08-28");
		SQL s = DynamicSQL.getSQL("log.selectAll", param);
		Dao dao = Dao.getInstance();
		List<Map<String, Object>> list = dao.find(s.getSql(), s.getPs().toArray());
		for (Map<String, Object> map : list)
		{
			System.out.println(map.get("method"));			
		}
	}
}
