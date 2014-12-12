package org.rzy.dao;

public class DataAccessException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	public DataAccessException()
	{
		super();
	}

	public DataAccessException(String message)
	{
		super(message);
	}

	public DataAccessException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DataAccessException(Throwable cause)
	{
		super(cause);
	}
}