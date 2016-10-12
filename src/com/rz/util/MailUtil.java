package com.rz.util;

import java.util.List;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class MailUtil
{
	@SuppressWarnings("deprecation")
	public static void sendEmail(String from, List<String> to, String subject, String content)
	{
		try
		{
			String hostname = "smtp.qq.com";
			String username = "602146904@qq.com";
			String password = "dlbqkvnyirtbbcde";
			String port = "25";
			String sender = "602146904@qq.com";
			SimpleEmail email = new SimpleEmail();
			email.setHostName(hostname);
			email.setAuthentication(username, password);
			email.setSSL(Boolean.TRUE);
			email.setSslSmtpPort(port);
			email.setFrom(sender);
			email.setSubject(subject);
			email.setMsg(content);
			email.addTo(to.toArray(new String[0]));
			email.send();
		}
		catch (EmailException e)
		{
			e.printStackTrace();
		}
	}
}
