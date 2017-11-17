package jone.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import jone.template.Engine;
import jone.template.Template;

import org.apache.commons.io.FilenameUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

public class Html2Pdf
{
	private static Engine engine = Engine.use();

	private Html2Pdf()
	{
	}

	public static void transform(String htmlFile, Map<String, Object> data, String pdfFile)
	{
		OutputStream out = null;
		try
		{
			String tplDir = FilenameUtils.getFullPath(htmlFile);
			String fileName = FilenameUtils.getName(htmlFile);
			engine.setBaseTemplatePath(tplDir);
			out = new FileOutputStream(pdfFile);
			Template t = engine.getTemplate(fileName);
			String html = t.renderToString(data);
			String font = "C:/Windows/fonts/simsun.ttc";
			String os = System.getProperty("os.name");
			if (os.toLowerCase().indexOf("linux") > -1)
			{
				font = "/usr/share/fonts/TTF/ARIALUNI.TTF";
			}
			ITextRenderer renderer = new ITextRenderer();
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont(font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			renderer.setDocumentFromString(html);
			renderer.layout();
			renderer.createPDF(out);
			renderer.finishPDF();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
