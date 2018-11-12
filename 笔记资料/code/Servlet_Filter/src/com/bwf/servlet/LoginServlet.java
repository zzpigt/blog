package com.bwf.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String uname = request.getParameter("uname");
		String upwd = request.getParameter("upwd");
		
		System.out.println("uname = " + uname);
		
		// 模拟登录成功, 必须是abc和123 才行
		if("abc".equals(uname) && "123".equals(upwd)){
			
			// session保存用户信息
			request.getSession().setAttribute("user", uname);
			
			// 重定向到登录后页面
			response.sendRedirect("/Servlet_Filter/user/success.jsp");
			
		} else {
			request.getSession().removeAttribute("user");
			// 重定向到显示登录失败的页面
			response.sendRedirect("/Servlet_Filter/user/fail.jsp");
		}
		
	}

}
