package jone.web;

public interface Interceptor
{
	Object intercept(Action action) throws Exception;
}
