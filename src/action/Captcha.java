package action;

import com.rz.web.CaptchaKit;

public class Captcha
{
	public void execute()
	{
		CaptchaKit.create(75, 35);
	}
}
