package com.MyBBS;

import java.io.*;
import java.util.*;
import com.MyBBS.Controller;
import com.MyBBS.CommentData;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "BBS", urlPatterns = { "/login" })
public class LoginPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			response.getWriter().println("<html>");
			response.getWriter().println("<head>");	
			response.getWriter().println("<title>login page</title>");
			response.getWriter().println("</head>");
			response.getWriter().println("<body>");
			response.getWriter().println("<h1>login</h1>");
			response.getWriter().println("<form action=\"/MyBBS/login\" method=\"post\">");
			response.getWriter().println("login ID:<input type=\"text\" name=\"loginid\"><br>");
			response.getWriter().println("Password:<input type=\"text\" name=\"loginid\"><br>");
			response.getWriter().println("<input type=\"submit\" value=\"send\">");
			response.getWriter().println("</form>");
			response.getWriter().println("</body>");
			response.getWriter().println("</html>");
		}catch (Exception e) {
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
		}catch (Exception e) {
			
		}
	}
	
}
