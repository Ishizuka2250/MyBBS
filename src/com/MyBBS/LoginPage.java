package com.MyBBS;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.MyBBS.LoginController;

/**
 * Servlet implementation class LoginPage
 */
@WebServlet(name = "LoginPage", urlPatterns = { "/login" })
public class LoginPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static LoginController controller;
	private String LoginResult = "";
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
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getWriter().println("<html>");
			response.getWriter().println("<head>");
			response.getWriter().println("<meta charset=\"UTF-8\">");
			response.getWriter().println("<title>login page</title>");
			response.getWriter().println("</head>");
			response.getWriter().println("<body>");
			response.getWriter().println("<h1>login page</h1>");
			
			if(LoginResult.equals("Password NG")) response.getWriter().println("<p><font color=\"red\">※パスワードが間違っています。</font></p>");
			else if(LoginResult.equals("LoginID is NotFound")) response.getWriter().println("<p><font color=\"red\">※ログインIDが存在しません。</font></p>");
			response.getWriter().println("<form action=\"/MyBBS/login\" method=\"post\">");
			response.getWriter().println("login ID:<input type=\"text\" name=\"loginid\"><br>");
			response.getWriter().println("Password:<input type=\"password\" name=\"password\"><br>");
			response.getWriter().println("<p><input type=\"submit\" value=\"send\">");
			response.getWriter().println("<span style=\"padding-left: 15px;\"><a href=\"/MyBBS/newuser\">新規ID登録</span></p>");
			response.getWriter().println("</form>");
			response.getWriter().println("</body>");
			response.getWriter().println("</html>");
		}catch (Exception e) {
			
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
            
            if (LoginResult.equals("SQLException")) {
            	serverError(response,controller.getSQLStackTrace());
            	return;
            }
            
            if (LoginResult.equals("Password OK")) {
                session = request.getSession(true);
                session.setMaxInactiveInterval(30);
                response.sendRedirect("/MyBBS/admin");
            }else if((LoginResult.equals("Password NG")) || (LoginResult.equals("LoginID is NotFound"))){
                response.sendRedirect("/MyBBS/login");
            }
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			
			serverError(response,sw);
		}
	}

	/**
	 * エラー発生時のスタックトレースを表示します。　
	 * @param response HTTPレスポンス
	 * @param sw 表示するスタックトレース
	 * @throws ServletException
	 * @throws IOException
	 */
	public void serverError(HttpServletResponse response,StringWriter sw)  throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html>");
        response.getWriter().println("<head>");
		response.getWriter().println("<meta charset=\"UTF-8\">");
        response.getWriter().println("<title>500</title>");
        response.getWriter().println("<head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>500 Server Error</h1>"); 
        response.getWriter().println("<p>");
        response.getWriter().println(sw.toString());
        response.getWriter().println("</p>");
        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
	}

}

