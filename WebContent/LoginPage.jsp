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
      }

      if(document.LoginForm.PasswordForm.value == "") {
        emptyFlg = true;
        document.getElementById("emptyWarnPassword").innerHTML = "<font color=\"red\">パスワードが入力されていません</font>";
      }

      if(emptyFlg == false) document.LoginForm.submit();
    }

    function formReset() {
      document.LoginForm.IDForm.value = "";
      document.LoginForm.PasswordForm.value = "";
      document.getElementById("emptyWarnID").innerHTML = "";
      document.getElementById("emptyWarnPassword").innerHTML = "";
    }
-->
    </script>
  </head>
  <body>
    <h2>管理者ログイン</h2>
		<%%>
    <form action="/MyBBS/login" method="post" name="LoginForm">
      <table border="0">
        <tr>
          <td>login ID:</td>
          <td><input type="text" id="IDForm" name="loginid"></td>
          <td><span id="emptyWarnID"><font color="red"></font></span></td>
        </tr>
        <tr>
          <td>Password:</td>
          <td><input type="password" id="PasswordForm" name="password"></td>
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
