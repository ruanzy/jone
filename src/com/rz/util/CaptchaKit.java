package com.rz.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import com.rz.web.ActionContext;

public class CaptchaKit
{
	static String CAPTCHAKEY = "RZY_CAPTCHA";

	private CaptchaKit()
	{

	}

	public static void create(int width, int height)
	{
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(getRandomColor(210, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Courier New", Font.BOLD, 20));
		g.drawRect(0, 0, width, height);

		g.setColor(getRandomColor(150, 250));
		Random random = new Random();
		for (int i = 0; i < 155; i++)
		{
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			g.drawLine(x, y, x + x1, y + y1);
		}
		String randomStr = "";
		String rchars = "1234567890ABCDEFGHJKLMNPQRSTUVWXYZ";
		for (int i = 0; i < 4; i++)
		{
			int len = rchars.length();
			int randomNum = random.nextInt(len);
			String rand = rchars.substring(randomNum, randomNum + 1);
			randomStr += rand;
			g.setColor(new Color(25 + random.nextInt(110), 25 + random.nextInt(110), 25 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 12, height * 2 / 3);
		}
		g.dispose();
		ActionContext.get().getRequest().getSession(true).setAttribute(CAPTCHAKEY, randomStr.toLowerCase());
		try
		{
			ImageIO.write(img, "JPEG", ActionContext.get().getResponse().getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static Color getRandomColor(int fc, int bc)
	{
		Random random = new Random();
		if (fc > 255)
		{
			fc = 255;
		}
		if (bc > 255)
		{
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public static String get()
	{
		HttpSession s = ActionContext.get().getRequest().getSession(false);
		return (String)(s.getAttribute(CAPTCHAKEY));
	}
}
