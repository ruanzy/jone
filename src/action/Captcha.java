package action;

import com.rz.util.CaptchaKit;

public class Captcha
{
	public void execute()
	{
		CaptchaKit.create(75, 35);
	}
}
