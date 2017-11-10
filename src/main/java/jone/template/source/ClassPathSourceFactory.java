package jone.template.source;

/**
 * ClassPathSourceFactory 用于配置 Engine 使用 ClassPathSource 加载模板文件
 * 
 * 配置示例：
 *    engine.baseTemplatePath(null);	// 清掉 base path
 *    engine.setSourceFactory(new ClassPathSourceFactory());
 */
public class ClassPathSourceFactory implements ISourceFactory {
	
	public ISource getSource(String baseTemplatePath, String fileName, String encoding) {
		return new ClassPathSource(baseTemplatePath, fileName, encoding);
	}
}



