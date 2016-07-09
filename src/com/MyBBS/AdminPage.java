package com.MyBBS;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AdminPage
 */
@WebServlet(name = "AdminPage", urlPatterns = {"/admin"})
public class AdminPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminPage() throws ClassNotFoundException{
        // TODO Auto-generated constructor stub
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
        HttpSession session = request.getSession(false);
        
		if(session == null) {
			response.sendRedirect("/MyBBS/login");
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
        response.getWriter().println("<html>");
        response.getWriter().println("<head>");
		response.getWriter().println("<meta charset=\"UTF-8\">");
        response.getWriter().println("<title>Admin Page</title>");
        response.getWriter().println("<head>");
        response.getWriter().println("<body>");
        response.getWriter().println("<h1>Session Check</h1>");
        response.getWriter().println("<form action=\"/MyBBS/admin\" method=\"post\">");
        response.getWriter().println("<input type=\"submit\" value=\"Logout\"><br>");
        response.getWriter().println("</form>");
        if (session != null) {
        	response.getWriter().println("<p>Session OK</p>");
        }else{
        	response.getWriter().println("<p>Session NG</p>");
        }
        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session != null) session.invalidate();
		response.sendRedirect("/MyBBS/login");
		// TODO Auto-generated method stub
	}

}
