package com.bwf.filter;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import com.sun.org.apache.xerces.internal.impl.io.UTF8Reader;

@WebFilter(dispatcherTypes = {DispatcherType.REQUEST }
					, urlPatterns = { "/*" })
public class EncodingFilter implements Filter {


	

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// request �������Ǻ���Servlet���õ���request
		// ϣ����ǿ 
		
		// ��request��װһ��
		Utf8Request nRequest = new Utf8Request((HttpServletRequest) request);
		
		
		chain.doFilter(nRequest, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
	public void destroy() {
	}

}
