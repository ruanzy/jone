package com.rz.web;

import java.io.InputStream;

public class Multipart {
	private String fieldName;
	private String fileName;
	private long fileSize;
	private String contentType;
	private InputStream inputStream;

	public Multipart(String fieldName, String fileName, long fileSize,
			String contentType, InputStream inputStream) {
		this.fieldName = fieldName;
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.contentType = contentType;
		this.inputStream = inputStream;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFileName() {
		return fileName;
	}

	public long getFileSize() {
		return fileSize;
	}

	public String getContentType() {
		return contentType;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
}
