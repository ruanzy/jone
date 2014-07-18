package rzy.core;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultHandler
{
	void handler(ResultSet rs) throws SQLException;
}
