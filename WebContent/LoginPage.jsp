<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8">
    <title>login page</title>
    <script type="text/javascript">
<!--
function emptyCheck() {
  var emptyFlg = false;
  if(document.LoginForm.IDForm.value == "") {
    emptyFlg = true;
    document.getElementById("emptyWarnID").innerHTML = "<font color=\"red\">IDが入力されていません</font>";
  }else{
    document.getElementById("emptyWarnID").innerHTML = "<span></span>";
  }

  if(document.LoginForm.PasswordForm.value == "") {
    emptyFlg = true;
    document.getElementById("emptyWarnPassword").innerHTML = "<font color=\"red\">パスワードが入力されていません</font>";
  }else{
    document.getElementById("emptyWarnPassword").innerHTML = "<span></span>";
  }

  if(emptyFlg == false) document.LoginForm.submit();
}

function formReset() {
  document.LoginForm.IDForm.value = "";
  document.LoginForm.PasswordForm.value = "";
  document.getElementById("emptyWarnID").innerHTML = "";
  document.getElementById("emptyWarnPassword").innerHTML = "";
}

function enter() {
  if(window.event.keyCode == 13) emptyCheck();
}
-->
    </script>
  </head>
  <body>
    <h2>管理者ログイン</h2>
		<%
		if(request.getAttribute("LoginSessionTimeout") != null) {
		  try{
        if((boolean)request.getAttribute("LoginSessionTimeout") == true) {
          out.println("<a>セッションがタイムアウトしました。</a>");
        }
		  }catch (Exception e) {
		    log("null pointer exception");
		  }
		}
		
		if(request.getAttribute("LoginResult") != null) {
		  if((boolean)request.getAttribute("LoginResult") == false) {
		    out.println("<a><font color=\"red\">ログインID もしくは パスワード が間違っています。</font></a>");
		  }
		}
		%>
    <form action="/MyBBS/login" method="post" name="LoginForm">
      <table border="0">
        <tr>
          <td>login ID:</td>
          <td><input type="text" id="IDForm" name="loginid"></td>
          <td><span id="emptyWarnID"><font color="red"></font></span></td>
        </tr>
        <tr>
          <td>Password:</td>
          <td><input type="password" id="PasswordForm" name="password" onkeydown="enter();"></td>
          <td><span id="emptyWarnPassword"><font color="red"></font></span></td>
        </tr>
        <tr>
          <td><input type="button" value="送信" id="SubmitButton" onclick="emptyCheck();"></td>
          <td><input type="button" value="リセット" id="ResetButton" onclick="formReset();"></td>
        </tr>
      </table>
    </form>
  </body>
</html>
