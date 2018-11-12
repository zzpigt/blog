package com.bwf.servlet;

import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bwf.bean.User;

@WebServlet("/t3")
public class Test3Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 往Session中放入一个User对象
		
		User u1 = new User("张三", 18);
		User u2 = new User("李四", 19);
		
		request.getSession().setAttribute("user", u2);
		
	}

}
