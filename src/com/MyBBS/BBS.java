package com.MyBBS;

import java.io.*;
import java.util.*;
import com.MyBBS.Controller;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * �R�����g�̕\���E���e���s���܂��B<br>
 * Tomcat��Ŗ{�N���X��URL��http://***.***.***.***:8080/MyBBS/ �ɂȂ�܂��B
 */

@WebServlet(name = "BBS", urlPatterns = { "/" })
public class BBS extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Controller controller;
	
    /**
     * �R�����g�̕\���E���e�\�Ȍf���y�[�W�𐶐����܂��B
     * @see HttpServlet#HttpServlet()
     * @throws ClassNotFoundException
     */
    public BBS() throws ClassNotFoundException {
        super();
        // TODO Auto-generated constructor stub
        controller = new Controller();
    }

	/**
	 * GET���N�G�X�g��
	 * ���e���ꂽ�R�����g�̕\�����s���܂��B
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			//HTML
			response.setContentType("text/html");
			response.getWriter().println("<html>");
			response.getWriter().println("<head>");
			response.getWriter().println("<title>BBS</title>");
			response.getWriter().println("</head>");
			response.getWriter().println("<body>");
			response.getWriter().println("<a href=\"/MyBBS/login\">Login</a><br>");
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
			
			response.setContentType("text/html");
			response.getWriter().println("<html>");
			response.getWriter().println("<head>");
			response.getWriter().println("<title>Error</title>");
			response.getWriter().println("</head>");
			response.getWriter().println("<body>");
			response.getWriter().println("<h1>500 Server Error</h1>");
			response.getWriter().println("<p>");
			response.getWriter().println(sw.toString());
			response.getWriter().println("</p>");
			response.getWriter().println("</body>");
			response.getWriter().println("</html>");
		}
		
	}

	/**
	 * ���e���e���f�[�^�x�[�X�����N���X�֑��M���A���e���ꂽ�R�����g�̈ꗗ��\�����܂��B
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String nowdate = controller.getDate();
		String comment = request.getParameter("comment");
		String name = request.getParameter("name");
		
		try {
			boolean commit_result = controller.commit(name, nowdate, comment);
			//HTML
			response.setContentType("text/html");
			response.getWriter().println("<html>");
			response.getWriter().println("<head>");
			response.getWriter().println("<title>BBS</title>");
			response.getWriter().println("<head>");
			response.getWriter().println("<body>");
			response.getWriter().println("<a href=\"/MyBBS/LOGIN/\">Login</a><br>");
			if (commit_result == false) response.getWriter().println(controller.getSQLStackTrace());
			printCommentLine(response);
			//response.getWriter().println("update:" + controller.updateCommentNo());
			response.getWriter().println("<form action=\"/MyBBS/BBS\" method=\"post\">");
			response.getWriter().println("Name: <input type=\"text\" name=\"name\"><br>");
			response.getWriter().println("<textarea name=\"comment\" rows=\"6\" cols=\"40\"></textarea><br>");
			response.getWriter().println("<input type=\"submit\" value=\"send\">");
			response.getWriter().println("</form>");
			response.getWriter().println("</body>");
			response.getWriter().println("</html>");
			
		}catch(Exception e){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
			
			printErrorMsg(response,sw);
		}
	}
	
	/**
	 * �G���[�������̃X�^�b�N�g���[�X��\�����܂��B�@
	 * @param response HTTP���X�|���X
	 * @param sw �\������X�^�b�N�g���[�X
	 * @throws ServletException
	 * @throws IOException
	 */
	public void printErrorMsg(HttpServletResponse response,StringWriter sw)  throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<html>");
        response.getWriter().println("<head>");
        response.getWriter().println("<title>BBS</title>");
        response.getWriter().println("<head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<p>");
        response.getWriter().println(sw.toString());
        response.getWriter().println("</p>");
        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
	}
	
	/**
	 * �f�[�^�x�[�X���瓊�e�R�����g�̈ꗗ��\�����܂�
	 * @param response HTTP���X�|���X
	 * @throws ServletException
	 * @throws IOException
	 */
	public void printCommentLine(HttpServletResponse response) throws ServletException, IOException {
		int i;
		int LastCommentLine;
		LastCommentLine = controller.updateCommentNo();
		
		//response.getWriter().println(LastCommentLine);
        if (controller.getCommentData() == false) {
			printErrorMsg(response,controller.getSQLStackTrace());
			return;
		}
		
		ArrayList<Integer> NoList = controller.getNoList();
		ArrayList<String> NameList = controller.getNameList();
		ArrayList<String> DateList = controller.getDateList();
		ArrayList<String> CommentList = controller.getCommentList();
        
        
		for (i=0;i<LastCommentLine;i++) {
            response.getWriter().println("<p>");
            response.getWriter().println(NoList.get(i) + ":" + NameList.get(i) + " " + DateList.get(i) + "<br />");
            response.getWriter().println(CommentList.get(i) + "<br />");
            response.getWriter().println("</p>");
		}
		
	}

}
