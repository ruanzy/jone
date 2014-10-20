package org.rzy.web;

import org.rzy.dao.Dao;
import com.alibaba.fastjson.JSON;

public class DBLogHandler implements LogHandler
{

	public void handler(Log log)
	{
		String user = log.getUser();
		String ip = log.getIP();
		String time = log.getTime();
		String sid = log.getSid();
		String op = WebUtil.getOP(sid);
		Object[] args = log.getArgs();
		String requestBody = JSON.toJSONString(args);
		String sql = "insert into log(id,operator,ip,time,op,method,result,memo) values(?,?,?,?,?,?,?,?)";
		Dao dao = Dao.getInstance();
		try
		{
			dao.begin();
			int id = dao.getID("log");
			Object[] params = new Object[] { id, user, ip, time, op, sid, 1, requestBody };
			dao.update(sql, params);
			dao.commit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			dao.rollback();
		}
	}

}
