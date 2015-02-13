package com.rz.web;

import java.io.IOException;
import javax.servlet.ServletException;

public interface ActionHandler
{
	void handle(ActionContext ac) throws IOException, ServletException;
}
