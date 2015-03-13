package com.rz.util;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class ClassUtil
{

	public static Map<String, String> getMethodVariable(String classname, String methodname)
	{
		Map<String, String> map = new HashMap<String, String>();
		try
		{
			ClassPool pool = ClassPool.getDefault();
			CtClass cc = pool.get(classname);
			CtMethod cm = cc.getDeclaredMethod(methodname);
			CtClass[] pt = cm.getParameterTypes();
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			String[] paramNames = new String[cm.getParameterTypes().length];
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			if (attr != null)
			{
				int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
				for (int i = 0; i < paramNames.length; i++)
				{
					map.put(pt[i].getName(), attr.variableName(i + pos));
				}
				return map;
			}
		}
		catch (Exception e)
		{

		}
		return null;
	}

	public static void main(String[] args)
	{
		Map<String, String> m = getMethodVariable("service.PmsService", "finduser");
		for (Map.Entry<String, String> entry : m.entrySet())
		{
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}
}
