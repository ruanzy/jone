package action;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import com.rz.web.WebUtil;

public class Doc
{
	public void down()
	{
		String file = WebUtil.getParameter("file");
		WebUtil.Response.get().setHeader("Content-type", "text/plain;charset=UTF-8");
		WebUtil.Response.get().setHeader("Content-Disposition", "attachment; filename=" + file);
		WebUtil.Response.get().setCharacterEncoding("UTF-8");
		String path = WebUtil.getWebRoot() + "/WEB-INF/download/" + file;
		OutputStream pw = null;
		try
		{
			pw = WebUtil.Response.get().getOutputStream();
			IOUtils.copy(new FileInputStream(path), pw);
		}
		catch (Exception e)
		{

		}
		finally
		{
			try
			{
				pw.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
