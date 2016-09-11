package com.MyBBS;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import com.MyBBS.LoginController;

/**
 * Servlet implementation class LoginPage
 */
@WebServlet(name = "LoginPage", urlPatterns = { "/login" })
public class LoginPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static LoginController controller;
	private int LoginResult = 0;
	private HttpSession session = null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginPage() throws ClassNotFoundException{
        super();
    	controller = new LoginController();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
		  HttpSession session = null;
		  session = request.getSession(false);
		  
		  if (session == null) {
		    response.sendRedirect("/MyBBS/LoginPage.jsp");
		    return;
		  }
		  
		  if (session.getAttribute("LastLogin") == null) {
		    response.sendRedirect("/MyBBS/LoginPage.jsp");
		    return;
		  }
		  
		  if (controller.LoginSessionCheck((String)session.getAttribute("LastLogin")) == false) {
		    response.sendRedirect("/MyBBS/LoginPage.jsp");
		    return;
		  }
		  
		  response.sendRedirect("/MyBBS/admin");
		  
		  /*response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getWriter().println("<html>");
			response.getWriter().println("<head>");
			response.getWriter().println("<meta charset=\"UTF-8\">");
			response.getWriter().println("<title>login page</title>");
			response.getWriter().println("</head>");
			response.getWriter().println("<body>");
			response.getWriter().println("<h1>掲示板管理者ログイン</h1>");
			
			if((LoginResult == 1) || (LoginResult == 2)) response.getWriter().println("<p><font color=\"red\">※ログインIDもしくはパスワードが間違っています。</font></p>");
			response.getWriter().println("<form action=\"/MyBBS/login\" method=\"post\">");
			response.getWriter().println("login ID:<input type=\"text\" name=\"loginid\"><br>");
			response.getWriter().println("Password:<input type=\"password\" name=\"password\"><br>");
			response.getWriter().println("<p><input type=\"submit\" value=\"send\">");
			//response.getWriter().println("<span style=\"padding-left: 15px;\"><a href=\"/MyBBS/newuser\">新規ID登録</span></p>");
			response.getWriter().println("</form>");
			response.getWriter().println("</body>");
			response.getWriter().println("</html>");*/
		}catch (Exception e) {
		  StringWriter sw = new StringWriter();
		  PrintWriter pw = new PrintWriter(sw);
		  e.printStackTrace(pw);
		  pw.flush();
		  serverError(request,response,sw);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
      String loginID = request.getParameter("loginid");
      String password = request.getParameter("password");
      
      //パスワード処理
      LoginResult = controller.LoginCheck(loginID, password);
      
      //SQL系エラー
      if (LoginResult == -1) {
        serverError(request,response,controller.getSQLStackTrace());
        return;
      }
        
      if (LoginResult == 0) {
        session = request.getSession(true);
        session.setMaxInactiveInterval(3600);//sec (60分)
        session.setAttribute("LastLogin",controller.getDate());
        session.setAttribute("LoginResult",true);
        response.sendRedirect("/MyBBS/admin");
      }else if((LoginResult == 1) || (LoginResult == 2)){
        request.setAttribute("LoginResult",false);
        session.setAttribute("LoginResult",false);
        RequestDispatcher rd = request.getRequestDispatcher("/LoginPage.jsp");
        rd.forward(request, response);
        return;
      }
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			serverError(request,response,sw);
		}
	}

	/**
	 * エラー発生時のスタックトレースを表示します。　
	 * @param response HTTPレスポンス
	 * @param sw 表示するスタックトレース
	 * @throws ServletException
	 * @throws IOException
	 */
    public void serverError(HttpServletRequest request,HttpServletResponse response, StringWriter sw)  throws ServletException, IOException {
      RequestDispatcher rd = null;
      log("stack"+ sw.toString());
      request.setAttribute("stackTrace", sw);
      rd = request.getRequestDispatcher("/ErrorPage.jsp");
      rd.forward(request,response);
    }

}

