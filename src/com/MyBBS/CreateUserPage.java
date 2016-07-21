package com.MyBBS;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.MyBBS.LoginController;

/**
 * Servlet implementation class CreateUserPage
 */
@WebServlet("/newuser")
public class CreateUserPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginController controller;
	private boolean createUserResult = false;
	private int DBCheckResult = 0;
	private boolean ServerError = false;
	private boolean UserNameEmpty = false;
	private boolean LoginIDEmpty = false;
	private boolean PasswordEmpty = false;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateUserPage() throws ClassNotFoundException{
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
			if(ServerError) {
				serverError(response,controller.getSQLStackTrace());
				return;
			}
			if(! createUserResult) {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html");
                response.getWriter().println("<html>");
                response.getWriter().println("<head>");
                response.getWriter().println("<meta charset=\"UTF-8\">");
                response.getWriter().println("<title>New UserPage</title>");
                response.getWriter().println("</head>");
                response.getWriter().println("<body>");
                response.getWriter().println("<h1>�V�K���[�U�[�쐬</h1>");
                response.getWriter().println("<form action=\"/MyBBS/newuser\" method=\"post\">");
                
                response.getWriter().println("���[�U�[��:<input type=\"text\" name=\"username\">");
                if (! UserNameEmpty) {
                    if (DBCheckResult == 1) response.getWriter().println("<font color=\"red\"> �����ɓo�^����Ă��郆�[�U�[���ł��B</font><br>");
                    else response.getWriter().println("<br>");
                }
                else response.getWriter().println("<font color=\"red\"> �����[�U�[�����͗����󗓂ł��B</font><br>");
                
                response.getWriter().println("Login ID:<input type=\"text\" name=\"loginid\">");
                if (! LoginIDEmpty) {
                    if (DBCheckResult == 2) response.getWriter().println("<font color=\"red\"> �����ɓo�^����Ă���LoginID�ł��B</font><br>");
                    else response.getWriter().println("<br>");
                }
                else response.getWriter().println("<font color=\"red\"> ��Login ID���͗����󗓂ł��B</font><br>");
                
                response.getWriter().println("Password:<input type=\"password\" name=\"password\">");
                if (! PasswordEmpty) response.getWriter().println("<br>");
                else response.getWriter().println("<font color=\"red\"> ��Password���͗����󗓂ł��B</font><br>");
                
                response.getWriter().println("<input type=\"submit\" value=\"send\">");
                response.getWriter().println("</form>");
                response.getWriter().println("</body>");
                response.getWriter().println("</html>");
			}else{
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html");
                response.getWriter().println("<html>");
                response.getWriter().println("<head>");
                response.getWriter().println("<meta charset=\"UTF-8\">");
                response.getWriter().println("<title>New UserPage</title>");
                response.getWriter().println("</head>");
                response.getWriter().println("<body>");
                response.getWriter().println("<h2>�o�^���������܂���</h2>");
                response.getWriter().println("<a href=\"/MyBBS/login\">���O�C���y�[�W��</a>");
                response.getWriter().println("</form>");
                response.getWriter().println("</body>");
                response.getWriter().println("</html>");
			}
			
		}catch (Exception e) {
			
		}
		UserNameEmpty = false;
		LoginIDEmpty = false;
		PasswordEmpty = false;
		DBCheckResult = 0;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String UserName = request.getParameter("username");
		String LoginID = request.getParameter("loginid");
		String Password = request.getParameter("password");
		
		if((UserName.equals("")) || (LoginID.equals("")) || (Password.equals(""))) {
			if(UserName.equals("")) UserNameEmpty = true;
			if(LoginID.equals("")) LoginIDEmpty = true;
			if(Password.equals("")) PasswordEmpty = true;
			response.sendRedirect("/MyBBS/newuser");
			return;
		}
		
		//DB�d���o�^�m�F
		DBCheckResult = controller.DataCheck(UserName, LoginID);
		if((DBCheckResult == 1) || (DBCheckResult == 2)) {
			response.sendRedirect("/MyBBS/newuser");
			return;
		}
		
		//DB�o�^����
		createUserResult = controller.createUser(UserName, LoginID, Password);
		if(! createUserResult) ServerError = true;
		response.sendRedirect("/MyBBS/newuser");
	}

	/**
	 * �G���[�������̃X�^�b�N�g���[�X��\�����܂��B�@
	 * @param response HTTP���X�|���X
	 * @param sw �\������X�^�b�N�g���[�X
	 * @throws ServletException
	 * @throws IOException
	 */
	public void serverError(HttpServletResponse response,StringWriter sw)  throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
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
        
        createUserResult = true;
        ServerError = false;
	}

}












