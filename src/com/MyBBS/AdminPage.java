package com.MyBBS;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import com.MyBBS.LoginController;

/**
 * Servlet implementation class AdminPage
 */
@WebServlet(name = "AdminPage", urlPatterns = {"/admin"})
public class AdminPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static LoginController controller;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminPage() throws ClassNotFoundException{
      // TODO Auto-generated constructor stub
      super();
      controller = new LoginController();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	  HttpSession session = request.getSession(false);
	  RequestDispatcher rd;
	  
	  if(session == null) {
	    log("/admin:session is null");
	    response.sendRedirect("/MyBBS/login");
	    return;
	  }
	  
	  if(session.getAttribute("LastLogin") == null) {
	    log("/admin:Not Login");
	    response.sendRedirect("/MyBBS/login");
	    return;
	  }
    
		if(controller.LoginSessionCheck((String)session.getAttribute("LastLogin")) == false) {
		  log("/admin:Session timeout");
		  request.setAttribute("LoginSessionTimeout", true);
		  rd = request.getRequestDispatcher("/LoginPage.jsp");
		  rd.forward(request, response);
		  return;
		}
		
		session.setAttribute("LastLogin",controller.getDate()); //セッションタイムの更新
		rd = request.getRequestDispatcher("/AdminPage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
}
