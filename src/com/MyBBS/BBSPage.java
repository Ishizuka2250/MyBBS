package com.MyBBS;

import java.io.*;
import java.util.*;
import com.MyBBS.BBSController;
import com.MyBBS.CommentData;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;

/**
 * �R�����g�̕\���E���e���s���܂��B<br>
 * Tomcat��Ōf���N���X��URL��http://***.***.***.***:8080/MyBBS/ �ɂȂ�܂��B
 */

@WebServlet(name = "BBSPage", urlPatterns = { "/BBSPage" })
public class BBSPage extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static BBSController Controller;
  private static boolean commitResult;
  private static boolean getCommentResult;

  /**
   * �R�����g�̕\���E���e�\�Ȍf���y�[�W�𐶐����܂��B
   * @see HttpServlet#HttpServlet()
   * @throws ClassNotFoundException
   */
  public BBSPage() throws ClassNotFoundException {
    super();
    // TODO Auto-generated constructor stub
    Controller = new BBSController();
  }

  /**
   * GET���N�G�X�g��<br>
   * ���e���ꂽ�R�����g�̕\�����s���܂��B
   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
   * @throws ServletException
   * @throws IOException
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    int i;
    int LastCommentLine;
    RequestDispatcher rd = null;
    String Buffer = "";
    try {
      LastCommentLine = Controller.updateCommentNo();
      getCommentResult = Controller.getCommentData();
      
      if (getCommentResult == false) {
        serverError(request, response, Controller.getSQLStackTrace());
        return;
      }
      
      ArrayList<CommentData> CommentDataList = Controller.getCommentDataList();
      ArrayList<String> CommentDataListString = new ArrayList<String>();
      
      for (i=0;i<LastCommentLine;i++) {
        Buffer = "<p>\n";
        Buffer += CommentDataList.get(i).No + ":" + CommentDataList.get(i).Name + " " + CommentDataList.get(i).CommentDate + "<br />\n";
        Buffer += CommentDataList.get(i).Comment+ "<br />\n";
        Buffer += "</p>\n";
        CommentDataListString.add(Buffer);
      }
      
      request.setAttribute("Comment", CommentDataListString);
      rd = request.getRequestDispatcher("/BBSPage.jsp");
      
    }catch(Exception e){
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
      pw.flush();
      
      serverError(request, response, sw);
      return;
    }finally {
      rd.forward(request, response);
    }
  }

  /**
   * POST���N�G�X�g��<br>
   * ���e���e���f�[�^�x�[�X�����N���X�֑��M���A���e���ꂽ�R�����g�̈ꗗ��\�����܂��B
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
   * @throws ServletException
   * @throws IOException
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // TODO Auto-generated method stub
    String postComment = request.getParameter("comment");
    String postName = request.getParameter("name");
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    if(postName == "") postName = "NoName";
    
    //���������΍�
    try{
      postComment = new String(postComment.getBytes("ISO-8859-1"),"UTF-8");
      postName = new String(postName.getBytes("ISO-8859-1"),"UTF-8");
    }catch (UnsupportedEncodingException e){
      e.printStackTrace(pw);
      pw.flush();
      serverError(request, response, sw);
      return;
    }
    
    //�󓊍e�̉��
    if (postComment.equals("")) {
      response.sendRedirect("/MyBBS/BBSPage");
      return;
    }
    
    try {
      commitResult = Controller.commit(postName, postComment);
      if(commitResult == false) {
        serverError(request,response,Controller.getSQLStackTrace());
        return;
      }
      response.sendRedirect("/MyBBS/BBSPage");
    }catch(Exception e){
      e.printStackTrace(pw);
      pw.flush();
      serverError(request,response,sw);
      return;
    }
  }

  /**
   * �G���[�����������B�X�^�b�N�g���[�X�\���y�[�W�֑J�ڂ��܂��B�@
   * @param response HTTP���X�|���X
   * @param sw �\������X�^�b�N�g���[�X
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
