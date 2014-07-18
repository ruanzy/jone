package rzy.util;

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
import rzy.core.Dao;
import rzy.core.ResultHandler;

public class ExcelDump
{
	public static void exp(String sql, final String file)
	{
		try
		{
			long begin = System.currentTimeMillis();
			HSSFWorkbook workbook = new HSSFWorkbook();
			final HSSFSheet sheet = workbook.createSheet();
			Dao.find(sql, new ResultHandler()
			{
				public void handler(ResultSet rs) throws SQLException
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
				}
			});
			workbook.write(new FileOutputStream(file));
			long end = System.currentTimeMillis();
			System.out.println("cross time:" + (end - begin) * 1.0 / 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void imp(String sql, String file)
	{
		long begin = System.currentTimeMillis();
		StringBuffer s = new StringBuffer(sql);
		try
		{
			HSSFWorkbook wrokbook = new HSSFWorkbook(new FileInputStream(file));
			HSSFSheet sheet = wrokbook.getSheetAt(0);
			int rows = sheet.getPhysicalNumberOfRows();
			HSSFRow row = null;
			for (int i = 1; i < rows; i++)
			{
				row = sheet.getRow(i);
				s.append("(");
				if (row != null)
				{
					int colCount = row.getPhysicalNumberOfCells();
					for (int j = 0; j < colCount; j++)
					{
						HSSFCell cell = row.getCell(j);
						int type = cell.getCellType();
						if (type == 0)
						{
							if (DateUtil.isCellDateFormatted(cell))
							{
								SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
								s.append("'").append(df.format(cell.getDateCellValue())).append("'");
							}
							else
							{
								double d = cell.getNumericCellValue();
								long l = Math.round(d);
								if (Double.parseDouble(l + ".0") == d)
									s.append(l);
								else
									s.append(d);
							}
						}
						if (type == 1)
						{
							s.append("'").append(cell.getStringCellValue()).append("'");
						}
						if (j != colCount - 1)
						{
							s.append(",");
						}
					}
				}
				s.append(")");
				if (i != rows - 1)
				{
					s.append(",");
				}
			}
			Dao.begin();
			Dao.update(sql.toString());
			Dao.commit();
			long end = System.currentTimeMillis();
			System.out.println("cross time:" + (end - begin) * 1.0 / 1000);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		imp("insert into users(id, username, pwd, regtime) values", "D://import.xls");
		// exp("select * from users", "D://export.xls");
	}
}