package org.rzy.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
	
	public static boolean isEmpty(String str)
	{
		return (str == null) || (str.length() == 0);
	}

	public static boolean isNotEmpty(String str)
	{
		return !isEmpty(str);
	}

	public static boolean isBlank(String str)
	{
		int strLen;
	    if ((str == null) || ((strLen = str.length()) == 0)) {
	      return true;
	    }
	    for (int i = 0; i < strLen; i++) {
	      if (!Character.isWhitespace(str.charAt(i))) {
	        return false;
	      }
	    }
	    return true;
	}

	public static boolean isNotBlank(String str)
	{
		return !isBlank(str);
	}
	
	public static String capitalize(String str)
	{
	    if ((str == null) || (str.length() == 0)) {
	      return str;
	    }
	    return Character.toTitleCase(str.charAt(0)) + str.substring(1);
	}
	
	public static String[] split(String str, char separatorChar)
	{
	    if (str == null) {
	        return null;
	      }
	      int len = str.length();
	      if (len == 0) {
	        return new String[0];
	      }
	      List<String> list = new ArrayList<String>();
	      int i = 0; int start = 0;
	      boolean match = false;
	      while (i < len)
	        if (str.charAt(i) == separatorChar) {
	          if ((match) || (false)) {
	            list.add(str.substring(start, i));
	            match = false;
	          }
	          i++; start = i;
	        }
	        else {
	          match = true;
	          i++;
	        }
	      if ((match) || false) {
	        list.add(str.substring(start, i));
	      }
	      return (String[])list.toArray(new String[list.size()]);
	}
	
	 public static String substringBeforeLast(String str, String separator)
	  {
	    if ((isEmpty(str)) || (isEmpty(separator))) {
	      return str;
	    }
	    int pos = str.lastIndexOf(separator);
	    if (pos == -1) {
	      return str;
	    }
	    return str.substring(0, pos);
	  }

	  public static String substringAfterLast(String str, String separator)
	  {
	    if (isEmpty(str)) {
	      return str;
	    }
	    if (isEmpty(separator)) {
	      return "";
	    }
	    int pos = str.lastIndexOf(separator);
	    if ((pos == -1) || (pos == str.length() - separator.length())) {
	      return "";
	    }
	    return str.substring(pos + separator.length());
	  }
	  
	  public static int toInt(String str, int defaultValue)
	  {
	    try
	    {
	      return Integer.parseInt(str); } catch (NumberFormatException nfe) {
	    }
	    return defaultValue;
	  }
}
