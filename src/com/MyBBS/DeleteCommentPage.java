package com.MyBBS;

import java.io.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import com.MyBBS.LoginController;
import com.MyBBS.BBSController;
import com.MyBBS.CommentData;

/**
 * Servlet implementation class DeleteCommentPage
 */
@WebServlet("/delete")
public class DeleteCommentPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginController Login;
	private BBSController bbs;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteCommentPage() throws ClassNotFoundException{
        super();
        // TODO Auto-generated constructor stub
        Login = new LoginController();
        bbs = new BBSController();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	  RequestDispatcher rd;
	  HttpSession session = request.getSession(false);
	  
	  if(session == null) {
	    log("/delete:session is null");
	    response.sendRedirect("/MyBBS/login");
	    return;
	  }
	  
	  if(session.getAttribute("LastLogin") == null) {
	    log("/delete:Not Login");
	    response.sendRedirect("/MyBBS/login");
	    return;
	  }
    
		if(Login.LoginSessionCheck((String)session.getAttribute("LastLogin")) == false) {
		  log("/delete:Session timeout");
		  request.setAttribute("LoginSessionTimeout", true);
		  rd = request.getRequestDispatcher("/LoginPage.jsp");
		  rd.forward(request, response);
		  return;
		}
		
		session.setAttribute("LastLogin",Login.getDate()); //セッションタイムの更新
		//rd = request.getRequestDispatcher("/AdminPage.jsp");
		//rd.forward(request, response);


		ArrayList<CommentData> CommentDataList;
	  ArrayList<String> CommentDataListString;
	  int i;
	  String Buffer;
		
		if(bbs.getCommentData() == false) serverError(request,response,bbs.getSQLStackTrace());
	  
	  CommentDataList = bbs.getCommentDataList();
	  CommentDataListString = new ArrayList<String>();
		
    for (i=0;i<CommentDataList.size();i++) {
      Buffer = "<p>\n";
      Buffer += CommentDataList.get(i).No + ":" + CommentDataList.get(i).Name + " " + CommentDataList.get(i).CommentDate + "<br />\n";
      Buffer += CommentDataList.get(i).Comment+ "<br />\n";
      Buffer += "</p>\n";
      CommentDataListString.add(Buffer);
    }
		
		request.setAttribute("CommentDataList", CommentDataListString);
		rd = request.getRequestDispatcher("/DeleteCommentPage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	  int commentCount;
	  int i;
	  commentCount = bbs.getCommentCount();
	  for(i=0;i<commentCount;i++) {
	    if(request.getParameter(Integer.toString(i+1)) != null) {
	      //continue
	    }
	  }
	}
	
  public void serverError(HttpServletRequest request,HttpServletResponse response, StringWriter sw)  throws ServletException, IOException {
    RequestDispatcher rd = null;
    log("stack"+ sw.toString());
    request.setAttribute("stackTrace", sw);
    rd = request.getRequestDispatcher("/ErrorPage.jsp");
    rd.forward(request,response);
  }
}
