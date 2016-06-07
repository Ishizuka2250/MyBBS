package com.MyBBS;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Login
 */
@WebServlet(name = "Login",urlPatterns = "/login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.getWriter().println("<html>");
		response.getWriter().println("<head>");
		response.getWriter().println("<title>login</title>");
		response.getWriter().println("</head>");
		response.getWriter().println("<body>");
		response.getWriter().println("<form action=\"MyBBS/login\" method=\"post\">");
		response.getWriter().println("LoginID:<input type=\"text\" name\"loginID\"><br>");
		response.getWriter().println("password:<input type=\"text\" name\"password\"><br>");
		response.getWriter().println("<input type=\"submit\" value=\"send\">");
		response.getWriter().println("</form>");
		response.getWriter().println("</body>");
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.getWriter().println("<html>");
		response.getWriter().println("<head>");
		response.getWriter().println("<title>login</title>");
		response.getWriter().println("</head>");
		response.getWriter().println("<body>");
		response.getWriter().println("<p>login</p>");
		response.getWriter().println("</body>");
	}

}
