package org.rzy.web;

import java.util.List;
import java.util.Map;

public class User
{
	private String name;
	private String password;
	private List<Map<String, Object>> roles;
	private List<Map<String, Object>> resources;

	public User(String name, String password)
	{
		this.name = name;
		this.password = password;
	}

	public String getPassword()
	{
		return password;
	}

	public String getName()
	{
		return name;
	}

	public boolean isAdmin()
	{
		return ("admin").equals(name) && ("162534").equals(password);
	}

	public List<Map<String, Object>> getRoles()
	{
		return roles;
	}

	public void setRoles(List<Map<String, Object>> roles)
	{
		this.roles = roles;
	}

	public List<Map<String, Object>> getResources()
	{
		return resources;
	}

	public void setResources(List<Map<String, Object>> resources)
	{
		this.resources = resources;
	}
}
