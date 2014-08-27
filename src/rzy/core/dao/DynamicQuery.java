package rzy.core.dao;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
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

public class DynamicQuery
{
	static Map<String, String> sqls = new HashMap<String, String>();
	static
	{
		InputStream is = null;
		try
		{
			is = DynamicQuery.class.getClassLoader().getResourceAsStream("sql.xml");
			if (is == null)
			{
				is = new FileInputStream("sql.xml");
			}
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(is);
			Element root = doc.getDocumentElement();
			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();
			NodeList result = (NodeList) xpath.evaluate("/commands/sql", root, XPathConstants.NODESET);
			for (int i = 0; i < result.getLength(); i++)
			{
				Node n = result.item(i);
				String sqlid = n.getAttributes().getNamedItem("id").getNodeValue();
				String sql = n.getTextContent().replaceAll("\\s+|\t|\r|\n", " ").trim();
				sqls.put(sqlid, sql);
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException("Load sql failed", e);
		}
	}

	public static String getSQL(String sqlid, Map<String, ?> params)
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
}
