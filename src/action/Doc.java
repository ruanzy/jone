package action;

import com.rz.web.Download;
import com.rz.web.WebUtil;

public class Doc
{
	public void down()
	{
		String file = WebUtil.getParameter("file");
		String dir = WebUtil.getWebRoot() + "/WEB-INF/download";
		Download.down(dir, file);
	}
}
