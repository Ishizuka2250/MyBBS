<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>投稿コメントの削除</title>
    <script type="text/javascript">
<!--
function adminPage(){
	location.href="/MyBBS/admin"
}
function commentDelete(){
	result = confirm("選択したコメントを削除しますか？");
	if(result == true) {
		document.commentDeleteForm.submit();
	}
}
-->
    </script>
  </head>
  <body>
    <h2>投稿コメントの削除</h2>
    <form action="/MyBBS/delete" method="post" id="commentDeleteForm">
      <table border="1">
        <tr>
          <td>削除</td>
          <td align="center">投稿コメント</td>
        </tr>
        <%
        if(request.getAttribute("CommentDataList") != null) {
          int i;
          ArrayList<String> CommentData = (ArrayList<String>)request.getAttribute("CommentDataList");
          for (i=0;i<CommentData.size();i++) {
            out.println("<tr>");

            out.println("<td>");
            out.println("<input type=\"checkbox\" name=\"" + (i+1) + "\" value=\"true\">");
            out.println("</td>");

            out.println("<td>");
            out.println(CommentData.get(i));
            out.println("</td>");
            
            out.println("</tr>");
          }
        }
        %>
      </table>
      <br />
      <input type="button" value="削除" onclick="commentDelete();">
      <input type="button" value="戻る" onclick="adminPage();">
    </form>
  </body>
</html>