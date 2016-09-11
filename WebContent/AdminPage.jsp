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
        log("AdminPage:direct access AdminPage.jsp");
      }
    %>
    <h2>Admin Pages</h2>
    <p>password OK!</p>
    <form action=\MyBBS/admin method="post">
      <input type="submit" value="Logout"><br>
    </form>
  </body>
</html>