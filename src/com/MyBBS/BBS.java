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

/**
 * コメントの表示・投稿を行います。<br>
 * Tomcat上で掲示板クラスのURLはhttp://***.***.***.***:8080/MyBBS/ になります。
 */

@WebServlet(name = "BBS", urlPatterns = { "/" })
public class BBS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Controller controller;
	private static boolean ServerError_flg;
	private static boolean commitResult;
	private static boolean getCommentResult;
	
    /**
     * コメントの表示・投稿可能な掲示板ページを生成します。
     * @see HttpServlet#HttpServlet()
     * @throws ClassNotFoundException
     */
    public BBS() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
        controller = new Controller();
        ServerError_flg = false;
    }

	/**
	 * GETリクエスト時<br>
	 * 投稿されたコメントの表示を行います。
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			//サーバーエラー表示確認
			if(ServerError_flg) {
				serverError(response,controller.getSQLStackTrace());
				return;
			}
			
			//HTML	
			response.setContentType("text/html");
			response.getWriter().println("<html>");
			response.getWriter().println("<head>");
			response.getWriter().println("<title>BBS</title>");
			response.getWriter().println("</head>");
			response.getWriter().println("<body>");
			//response.getWriter().println("<a href=\"/MyBBS/login\">Login</a><br>");
			//response.getWriter().println("<div align=\"center\">");
			printCommentLine(response);
			//response.getWriter().println("update:" + controller.updateCommentNo());
			response.getWriter().println("<form action=\"/MyBBS/BBS\" method=\"post\">");
			response.getWriter().println("Name: <input type=\"text\" name=\"name\"><br>");
			response.getWriter().println("<textarea name=\"comment\" rows=\"6\" cols=\"40\"></textarea><br>");
			response.getWriter().println("<input type=\"submit\" value=\"send\">");
			response.getWriter().println("</form>");
			//response.getWriter().println("</div>");
			response.getWriter().println("</body>");
			response.getWriter().println("</html>");
			
		}catch(Exception e){
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			
			serverError(response,sw);
		}
	}

	/**
	 * POSTリクエスト時<br>
	 * 投稿内容をデータベース処理クラスへ送信し、投稿されたコメントの一覧を表示します。
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String postComment = request.getParameter("comment");
		String postName = request.getParameter("name");
	
		try {
			commitResult = controller.commit(postName, postComment);
			if (commitResult == false) ServerError_flg = true; 
			//response.getWriter().println(controller.getSQLStackTrace());
			response.sendRedirect("/MyBBS/");
		}catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
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
        response.getWriter().println("<title>500</title>");
        response.getWriter().println("<head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>500 Server Error</h1>"); 
        response.getWriter().println("<p>");
        response.getWriter().println(sw.toString());
        response.getWriter().println("</p>");
        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
        
        ServerError_flg = false;
	}
	
	/**
	 * データベースから投稿コメントの一覧を表示します
	 * @param response HTTPレスポンス
	 * @throws ServletException
	 * @throws IOException
	 */
	public void printCommentLine(HttpServletResponse response) throws ServletException, IOException {
		int i;
		int LastCommentLine;
		LastCommentLine = controller.updateCommentNo();
		
		getCommentResult = controller.getCommentData();
		
        if (getCommentResult == false) {
			//printErrorMsg(response,controller.getSQLStackTrace());
			ServerError_flg = true;
        	return;
		}
		
        ArrayList<CommentData> CommentDataList = controller.getCommentDataList();
        
		for (i=0;i<LastCommentLine;i++) {
            response.getWriter().println("<p>");
            response.getWriter().println(CommentDataList.get(i).No + ":" + CommentDataList.get(i).Name + " " + CommentDataList.get(i).CommentDate + "<br />");
            response.getWriter().println(CommentDataList.get(i).Comment+ "<br />");
            response.getWriter().println("</p>");
		}
		
	}

}
