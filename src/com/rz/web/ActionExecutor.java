package com.rz.web;

public class ActionExecutor
{
	public static void execute(Action action)
	{
		action.invoke();
	}
}
