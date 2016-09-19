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
        RequestDispatcher rd;
		    rd = request.getRequestDispatcher("/LoginPage.jsp");
		    rd.forward(request, response);
		    return;
		  }
		  
		  if (session.getAttribute("LastLogin") == null) {
		    RequestDispatcher rd;
		    rd = request.getRequestDispatcher("/LoginPage.jsp");
		    rd.forward(request,response);
		    return;
		  }
		  
		  if (controller.LoginSessionCheck((String)session.getAttribute("LastLogin")) == false) {
		    RequestDispatcher rd;
		    rd = request.getRequestDispatcher("/LoginPage.jsp");
		    rd.forward(request, response);
		    return;
		  }
		  
		  response.sendRedirect("/MyBBS/admin");
		  
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
        session.setMaxInactiveInterval(3600);//sessiontime (sec)(設定：60分[3600/sec])
        session.setAttribute("LastLogin",controller.getDate()); //セッションタイムの更新
        session.setAttribute("LoginResult",true);
        log("LoginPage:user:" + loginID + " -- login ok.");
        response.sendRedirect("/MyBBS/admin");
        return;
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

