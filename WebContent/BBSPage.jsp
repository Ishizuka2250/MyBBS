<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>BBS</title>
  </head>
  <body>
  	<h2>☆掲示板タイトル☆</h2>
	<%
		int i;
		ArrayList<String> Comment = (ArrayList<String>)request.getAttribute("Comment");
		if (Comment != null) {
			for(i=0;i<Comment.size();i++) {
				out.println(Comment.get(i));
			}
		}else{
			response.sendRedirect("/MyBBS/BBSPage");
		}
	%>
    <form action="/MyBBS/BBSPage" method="post">
      <table border="0">
        <tr>
          <td>Name: <input type="text" name="name"></td>
        </tr>
        <tr>
          <td><textarea name="comment" rows="7" cols="40"></textarea></td>
        </tr>
        <tr>
          <td><input type="submit" value="send"></td>
        </tr>
      </table>
    </form>
    <br>
    <a href="/MyBBS/login">管理者ページへ</a>
  </body>
</html>
