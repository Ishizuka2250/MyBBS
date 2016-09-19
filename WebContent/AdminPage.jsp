<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
  <head>
    <title>AdminPage</title>
    <meta charset="UTF-8">
  </head>
  <body>
    <%
      boolean LoginResult = false;
      if(session.getAttribute("LoginResult") != null) {
        LoginResult = (boolean)session.getAttribute("LoginResult");
        if(LoginResult == false) response.sendRedirect("/MyBBS/login");
      } else {
        response.sendRedirect("/MyBBS/login");
        log("AdminPage:direct access to AdminPage.jsp");
      }
    %>
    <h2>掲示板管理者ページ</h2>
    <p>password OK!</p>
    <p><a href="/MyBBS/delete">コメント削除</a></p>
    <form action="/MyBBS/Logout" method="post">
      <input type="submit" value="Logout"><br>
    </form>
  </body>
</html>