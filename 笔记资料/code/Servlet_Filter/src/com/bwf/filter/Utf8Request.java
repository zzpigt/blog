package com.bwf.filter;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class Utf8Request extends HttpServletRequestWrapper {

	public Utf8Request(HttpServletRequest request) {
		super(request);
	}
	
	@Override
	public String getParameter(String name) {
		// 先拿出原来的数据 -> 乱码
		String parameter = super.getParameter(name);
		
		String res = parameter;
		try {
			res = new String(parameter.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
}
