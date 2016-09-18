package com.rz.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.List;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class MongoUtil
{

	static Mongo mongo;

	static
	{
		try
		{
			mongo = new Mongo();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
	};

	public static void main(String[] args) throws Exception
	{
		// saveFile();
		readFile();
	}

	/**
	 * 存储文件
	 */
	public static void saveFile()
	{
		DB db = mongo.getDB("testGridFS");
		GridFS gridFS = null;
		gridFS = new GridFS(db);
		String fileName = "d:/upload/a.xml";
		String indexDir = "d:/Lindex3";
		File readFile = new File(fileName);
		GridFSInputFile mongofile = null;
		try
		{
			mongofile = gridFS.createFile(readFile);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mongofile.put("path", fileName);
		mongofile.save();
		LuceneUtil.indexFile(indexDir, readFile);
	}

	public static void saveFile(InputStream in, FileItem fileItem)
	{
		DB db = mongo.getDB("testGridFS");
		GridFS gridFS = null;
		gridFS = new GridFS(db);
		GridFSInputFile mongofile = gridFS.createFile(in);
		mongofile.putAll(fileItem.toMap());
		mongofile.save();
	}

	/**
	 * 读文件，读到磁盘上
	 * 
	 * @throws Exception
	 */
	public static void readFile() throws Exception
	{
		DB db = mongo.getDB("testGridFS");
		GridFS gridFs = null;
		gridFs = new GridFS(db);
		DBObject query = new BasicDBObject();
		List<GridFSDBFile> listfiles = gridFs.find(query);
		GridFSDBFile gridDBFile = listfiles.get(1);

		// 获得其中的文件名
		// 注意 ： 不是fs中的表的列名，而是根据调试gridDBFile中的属性而来
		String fileName = (String) gridDBFile.get("filename");

		String path = (String) gridDBFile.get("path");

		System.out.println("从Mongodb获得文件名为：" + fileName);

		System.out.println("path：" + path);

		File writeFile = new File(fileName);
		if (!writeFile.exists())
		{
			writeFile.createNewFile();
		}

		// 把数据写入磁盘中
		// 查看相应的提示
		gridDBFile.writeTo("d:/upload/a222.xml");
		// 写入文件中
		gridDBFile.writeTo(writeFile);

	}
}
