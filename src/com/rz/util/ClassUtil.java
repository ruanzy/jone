package com.rz.util;

import java.lang.reflect.Modifier;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class ClassUtil
{

	public static String[] getMethodVariableName(String classname, String methodname)
	{
		try
		{
			ClassPool pool = ClassPool.getDefault();
			CtClass cc = pool.get(classname);
			CtMethod cm = cc.getDeclaredMethod(methodname);
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
					paramNames[i] = attr.variableName(i + pos);
				}
				return paramNames;
			}
		}
		catch (Exception e)
		{
			
		}
		return null;
	}
	
	public static void main(String[] args)
	{
		String[] paramNames = getMethodVariableName("service.PmsService", "finduser");
		for (String string : paramNames)
		{
			System.out.println(string);
		}
	}
}
