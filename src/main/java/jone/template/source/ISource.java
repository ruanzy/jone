package jone.template.source;

/**
 * ISource 用于表示模板内容的来源
 */
public interface ISource {
	
	/**
	 * reload template if modified on devMode
	 */
	boolean isModified();
	
	/**
	 * key used to cache, return null if do not cache the template
	 * 
	 * 注意：如果不希望缓存从该 ISource 解析出来的 Template 对象
	 *      让 getKey() 返回 null 值即可  
	 */
	String getKey();
	
	/**
	 * content of ISource
	 */
	StringBuilder getContent();
	
	/**
	 * encoding of content
	 */
	String getEncoding();
}


