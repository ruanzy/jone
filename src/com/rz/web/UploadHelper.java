package com.rz.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadHelper {
	private static ServletFileUpload fileUpload;

	public static void init(int uploadLimit) {
		String temp = System.getProperty("java.io.tmpdir");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(new File(temp));
		factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
		fileUpload = new ServletFileUpload(factory);
		fileUpload.setHeaderEncoding("utf-8");
		if (uploadLimit > 0) {
			fileUpload.setSizeMax(uploadLimit * 1024 * 1024);
		}
	}

	public static List<Object> prepare() {
		boolean isMultipart = ServletFileUpload
				.isMultipartContent(WebUtil.Request.get());
		if (!isMultipart) {
			return null;
		}
		List<Object> paramList = new ArrayList<Object>();
		Map<String, String> fieldes = new HashMap<String, String>();
		List<Multipart> multiparts = new ArrayList<Multipart>();
		try {
			List<FileItem> list = fileUpload
					.parseRequest(WebUtil.Request.get());
			for (FileItem item : list) {
				String fieldName = item.getFieldName();
				if (item.isFormField()) {
					String v = item.getString();
					fieldes.put(fieldName, v);
				} else {
					String fileName = item.getName();
					long fileSize = item.getSize();
					String contentType = item.getContentType();
					InputStream inputSteam = item.getInputStream();
					Multipart multipart = new Multipart(fieldName, fileName,
							fileSize, contentType, inputSteam);
					multiparts.add(multipart);
				}
			}
			paramList.add(fieldes);
			paramList.add(multiparts);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramList;
	}

	public static void uploadFile(String dir, Multipart multipart) {
		try {
			if (multipart != null) {
				String srcName = multipart.getFileName();
				File savedFile = new File(dir, srcName);
				InputStream inputStream = new BufferedInputStream(
						multipart.getInputStream());
				OutputStream outputStream = new BufferedOutputStream(
						new FileOutputStream(savedFile));
				copyStream(inputStream, outputStream);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void copyStream(InputStream inputStream,
			OutputStream outputStream) {
		try {
			int length;
			byte[] buffer = new byte[4 * 1024];
			while ((length = inputStream.read(buffer, 0, buffer.length)) != -1) {
				outputStream.write(buffer, 0, length);
			}
			outputStream.flush();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (Exception e) {
			}
		}
	}
}
