package jone.template;

import jone.template.stat.Location;

/**
 * Template runtime exception
 */
@SuppressWarnings("serial")
public class TemplateException extends RuntimeException {
	
	public TemplateException(String msg, Location loc) {
		super(loc != null ? msg + loc : msg);
	}
	
	public TemplateException(String msg, Location loc, Throwable t) {
		super(loc != null ? msg + loc : msg, t);
	}
}


