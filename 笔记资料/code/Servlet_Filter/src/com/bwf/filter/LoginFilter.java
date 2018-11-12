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
import javax.servlet.http.HttpServletResponse;

@WebFilter(dispatcherTypes = {DispatcherType.REQUEST }
					, urlPatterns = { "/user/*" })
public class LoginFilter implements Filter {
	

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = ((HttpServletRequest)request);
		Object user = httpRequest.getSession().getAttribute("user");
		String url = httpRequest.getRequestURL().toString();
			
		
		// 如果登录了就放行, 或者访问的就是登录页面
		if(user != null || url.contains("login.jsp")){
			System.out.println("放行!");
			chain.doFilter(request, response);
		}else{
			System.out.println("没登录 跳转!");
			// 如果没登录, 就重定向到登录页面
			((HttpServletResponse)response).sendRedirect("/Servlet_Filter/user/login.jsp");
		}
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}
	public void destroy() {
	}
}
