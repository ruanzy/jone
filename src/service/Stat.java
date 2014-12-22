package service;

import org.rzy.dao.MongoDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stat
{
	private static Logger log = LoggerFactory.getLogger(Stat.class);
	public void browser()
	{
		String M = "function() { emit(this.browser, 1); }";
		String R = "function (key, values) { return Array.sum(values); }";
		MongoDao.MR("login", null, M, R, "login_browser_MR");
		log.debug("execute task browser...");
	}

	public void os()
	{
		String M = "function() { emit(this.os, 1); }";
		String R = "function (key, values) { return Array.sum(values); }";
		MongoDao.MR("login", null, M, R, "login_os_MR");
		log.debug("execute task os...");
	}

	public void user()
	{
		String M = "function() { emit(this.user, 1); }";
		String R = "function (key, values) { return Array.sum(values); }";
		MongoDao.MR("login", null, M, R, "login_user_MR");
		log.debug("execute task user...");
	}

	public void day()
	{
		String M = "function() { var k = this.time.substring(0, 4) + '-' + this.time.substring(5, 7) + '-' + this.time.substring(8, 10); emit({user:this.user, time:k}, 1);}";
		String R = "function (key, values) { return Array.sum(values); }";
		MongoDao.MR("login", null, M, R, "login_day_MR");
		log.debug("execute task day...");
	}

}