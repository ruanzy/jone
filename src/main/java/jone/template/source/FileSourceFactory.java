package jone.template.source;

/**
 * FileSourceFactory 用于配置 Engine 使用 FileSource 加载模板文件
 * 
 * 注意：
 *    FileSourceFactory 为模板引擎默认配置
 */
public class FileSourceFactory implements ISourceFactory {
	
	public ISource getSource(String baseTemplatePath, String fileName, String encoding) {
		return new FileSource(baseTemplatePath, fileName, encoding);
	}
}




