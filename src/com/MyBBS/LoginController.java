package com.MyBBS;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {
	private static String SqlitePath = "C:/Sqlite/BBS.sqlite3";
	private static StringWriter SQLStackTrace = new StringWriter();
	private static PrintWriter pw = new PrintWriter(SQLStackTrace);

	LoginController() throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
	}
	
	public String LoginCheck(String loginID,String password){
		String CheckResult = "";
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			
			statement.setQueryTimeout(30);
			
			ResultSet result = statement.executeQuery("select Password from LoginUser where LoginID = \"" + loginID + "\";");
			
			if (result.next()) {
				if (result.getString(1).equals(password)) CheckResult = "Password OK";
				else CheckResult = "Password NG";
			}else{
				CheckResult = "LoginID is NotFound";
			}
			
			statement.close();
			connection.close();
		}catch (SQLException e){
			e.printStackTrace(pw);
			pw.flush();
			return "SQLException";
		}
		
		return CheckResult;
	}
	
	public String DataCheck(String LoginName,String LoginID) {
		boolean LoginNameExist = false;
		boolean LoginIDExist = false;
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			ResultSet result = statement.executeQuery("select UserName from LoginUser where UserName = \"" + LoginName + "\";");
			if(result.next()) LoginNameExist = true;
			
			result = statement.executeQuery("select LoginID from LoginUser where LoginID = \"" + LoginID + "\";");
			if(result.next()) LoginIDExist = true;
			
		}catch (SQLException e){
			e.printStackTrace(pw);
			pw.flush();
			return "SQLException";
		}
		
		if(LoginNameExist) return "UserName is aleady used";
		if(LoginIDExist) return "LoginID is already used";
		
		return "OK";
	}
	
	public boolean createUser(String UserName,String LoginID,String Password) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			statement.execute("insert into LoginUser values(\"" + UserName +"\",\"" + LoginID + "\",\"" + Password + "\");");
			
			statement.close();
			connection.close();
		}catch (SQLException e) {
			e.printStackTrace(pw);
			pw.flush();
			return false;
		}
        return true;
	}

	/**
	 * データベース処理クラスで発生したSQLエラーのスタックトレースを返します。
	 * @return SQLエラーのスタックトレース
	 */
	public StringWriter getSQLStackTrace(){
		return SQLStackTrace;
	}

}
