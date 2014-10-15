package org.rzy.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DateUtil;
import org.rzy.dao.Dao;
import org.rzy.dao.ResultHandler;

public class DB2Excel
{
	public static void exp(String sql, final String file)
	{
		try
		{
			long begin = System.currentTimeMillis();
			HSSFWorkbook workbook = new HSSFWorkbook();
			final HSSFSheet sheet = workbook.createSheet();
			Dao dao = Dao.getInstance();
			dao.find(sql, new ResultHandler()
			{
				public void handle(ResultSet rs) throws SQLException
				{
					int row = 0;
					HSSFRow r = null;
					ResultSetMetaData rsmd = rs.getMetaData();
					int colCount = rsmd.getColumnCount();
					while (rs.next())
					{
						r = sheet.createRow(row++);
						HSSFCell cell = null;
						for (int i = 0; i < colCount; i++)
						{
							cell = r.createCell(i);
							cell.setCellValue(rs.getString(i + 1));
						}
					}
					System.out.println("成功导出" + row + "条数据");
				}
			});
			FileOutputStream fos = new FileOutputStream(file);
			workbook.write(fos);
			long end = System.currentTimeMillis();
			System.out.println("cross time:" + (end - begin) * 1.0 / 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally{
			
		}
	}

	public static void imp(String sql, String file)
	{
		long begin = System.currentTimeMillis();
		Dao dao = Dao.getInstance();
		try
		{
			HSSFWorkbook wrokbook = new HSSFWorkbook(new FileInputStream(file));
			HSSFSheet sheet = wrokbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			int size = 200;
			int n = 0;
			dao.begin();
			dao.beginBatch(sql);
			for (int i = 1; i <= rows; i++)
			{
				Object[] params = buildValues(sheet.getRow(i - 1));
				dao.addBatch(params);
				if (i % size == 0)
				{
					dao.excuteBatch();
					n++;
					String msg = String.format("成功导入第%s-%s条数据", (n - 1) * size + 1, n * size);
					System.out.println(msg);
				}
			}
			dao.excuteBatch();
			dao.endBatch();
			String msg = String.format("成功导入第%s-%s条数据", n * size + 1, rows);
			System.out.println(msg);
			dao.commit();
			long end = System.currentTimeMillis();
			System.out.println("cross time:" + (end - begin) * 1.0 / 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			dao.rollback();
		}
	}

	private static Object[] buildValues(HSSFRow row)
	{
		Object[] params = null;
		if (row != null)
		{
			int colCount = row.getPhysicalNumberOfCells();
			params = new Object[colCount];
			for (int j = 0; j < colCount; j++)
			{
				HSSFCell cell = row.getCell(j);
				int type = cell.getCellType();
				if (type == 0)
				{
					if (DateUtil.isCellDateFormatted(cell))
					{
						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
						params[j] = df.format(cell.getDateCellValue());
					}
					else
					{
						double d = cell.getNumericCellValue();
						long l = Math.round(d);
						if (Double.parseDouble(l + ".0") == d)
							params[j] = l;
						else params[j] = d;
					}
				}
				if (type == 1)
				{
					params[j] = cell.getStringCellValue();
				}
			}
		}
		return params;
	}

	public static void main(String[] args)
	{
		exp("select * from log", "D://export.xls");
		//imp("insert into log(id, operator, ip, time, method, result, memo) values(?,?,?,?,?,?,?)", "D://export.xls");
	}
}