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
		
		// ģ���¼�ɹ�, ������abc��123 ����
		if("abc".equals(uname) && "123".equals(upwd)){
			
			// session�����û���Ϣ
			request.getSession().setAttribute("user", uname);
			
			// �ض��򵽵�¼��ҳ��
			response.sendRedirect("/Servlet_Filter/user/success.jsp");
			
		} else {
			request.getSession().removeAttribute("user");
			// �ض�����ʾ��¼ʧ�ܵ�ҳ��
			response.sendRedirect("/Servlet_Filter/user/fail.jsp");
		}
		
	}

}
