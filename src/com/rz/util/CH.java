package com.rz.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class CH
{
	int replicas = 200;
	List<String> nodes = null;
	int counter1, counter2, counter3;
	Map<Long, String> mapper;

	public CH()
	{
		nodes = new ArrayList<String>();
		nodes.add("192.168.10.1");
		nodes.add("192.168.10.2");
		nodes.add("192.168.10.3");
		mapper = new TreeMap<Long, String>();
		for (String str : nodes)
		{
			for (int i = 1; i <= replicas; i++)
			{

				Long k = hash(str + "-" + i);

				mapper.put(k, str);
			}
		}
	}

	private long hash(String key)
	{
		MessageDigest md5 = null;
		try
		{
			md5 = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		md5.reset();
		md5.update(key.getBytes());
		byte[] bKey = md5.digest();
		long k = (long) (bKey[3] & 0xFF) << 24 | (long) (bKey[2] & 0xFF) << 16 | (long) (bKey[1] & 0xFF) << 8 | bKey[0]
				& 0xFF;
		return k & 0xffffffffL;
	}

	public void print()
	{
		for (Map.Entry<Long, String> entry : mapper.entrySet())
		{
			System.out.print(entry.getKey());
			System.out.print(":");
			System.out.print(entry.getValue());
			System.out.println("");
		}
	}

	public Set<Long> getKeys()
	{

		return mapper.keySet();
	}

	public void add(String str)
	{
		long dh1 = hash(str);
		Set<Long> ks = getKeys();
		Long dh2 = 0L;
		for (Long a : ks)
		{
			if (a >= dh1)
			{
				dh2 = a;
				break;
			}
		}
		String node = mapper.get(dh2);
		if ("192.168.10.1".equals(node))
		{
			counter1++;
		}
		else if ("192.168.10.2".equals(node))
		{
			counter2++;
		}
		else if ("192.168.10.3".equals(node))
		{
			counter3++;
		}
		// System.out.println(node);
	}

	public static String randomStr()
	{
		String randomStr = "";
		String rchars = "1234567890ABCDEFGHJKLMNPQRSTUVWXYZ";
		Random random = new Random();
		for (int i = 0; i < 4; i++)
		{
			int len = rchars.length();
			int randomNum = random.nextInt(len);
			String rand = rchars.substring(randomNum, randomNum + 1);
			randomStr += rand;
		}
		return randomStr;
	}

	public static void main(String[] args) throws NoSuchAlgorithmException
	{
		CH ch = new CH();
		// ch.print();
		Set<Long> ks = ch.getKeys();
		System.out.println(ks);
		int L = 10000;
		for (int i = 0; i < L; i++)
		{

			ch.add(randomStr() + i);
		}

		System.out.println(ch.counter1 * 1.0 / L);
		System.out.println(ch.counter2 * 1.0 / L);
		System.out.println(ch.counter3 * 1.0 / L);
	}
}
