package jone.template.stat.ast;

import java.io.IOException;
import java.io.Writer;

import jone.template.Env;
import jone.template.TemplateException;
import jone.template.stat.Scope;

/**
 * Text 输出纯文本块以及使用 "#[[" 与 "]]#" 指定的非解析块 
 */
public class Text extends Stat {
	
	private char[] text;
	
	public Text(StringBuilder content) {
		this.text = new char[content.length()];
		content.getChars(0, content.length(), this.text, 0);
	}
	
	public void exec(Env env, Scope scope, Writer writer) {
		try {
			writer.write(text, 0, text.length);
		} catch (IOException e) {
			throw new TemplateException(e.getMessage(), location, e);
		}
	}
	
	public boolean isEmpty() {
		return text.length == 0;
	}
	
	public String getContent() {
		return text != null ? new String(text) : null;
	}
	
	public String toString() {
		return text != null ? new String(text) : "";
	}
}



