package com.rz.data.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowHandler<T>
{
	T handle(ResultSet rs) throws SQLException;
}