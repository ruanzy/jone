package com.rz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowHandler<T>
{
	T handle(ResultSet rs) throws SQLException;
}