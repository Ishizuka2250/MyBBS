package com.MyBBS;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class LoginController {
	private static String SqlitePath = "C:/Sqlite/BBS.sqlite3";
	private static StringWriter SQLStackTrace = new StringWriter();
	private static PrintWriter pw = new PrintWriter(SQLStackTrace);
	private long SessionTimeOut = 1800;//sec (30分) 

	LoginController() throws ClassNotFoundException{
		Class.forName("org.sqlite.JDBC");
	}
	
	public int LoginCheck(String loginID,String password){
		int CheckResult = 0;
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			
			statement.setQueryTimeout(30);
			
			ResultSet result = statement.executeQuery("select Password from LoginUser where LoginID = \"" + loginID + "\";");
			
			if (result.next()) {
				if (result.getString(1).equals(password)) CheckResult = 0; //"Password OK";
				else CheckResult = 1; //"Password NG";
			}else{
				CheckResult = 2; //"LoginID is NotFound";
			}
			
			statement.close();
			connection.close();
		}catch (SQLException e){
			e.printStackTrace(pw);
			pw.flush();
			return -1;//"SQLException";
		}
		
		return CheckResult;
	}
	
	public int DataCheck(String LoginName,String LoginID) {
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
			return -1; //"SQLException"
		}
		
		if(LoginNameExist) return 1;// "UserName is already used"
		if(LoginIDExist) return 2;//"LoginID is already used"
		
		return 0;//"OK"
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
	
	public boolean LoginSessionCheck(String LastLogin){
	  String nowTime = getDate();
	  if(LastLogin == null) {
	    System.out.println("LastLogin:null");
	    return false;
	  }
	  long diffTime = diffSec(LastLogin,nowTime);
	  
	  //System.out.println("sessionCheck:" + SessionTimeOut + " > " + diffTime);
	  
	  if(SessionTimeOut > diffTime) return true;
	  else return false;
	}
	
	/**
	 * データベース処理クラスで発生したSQLエラーのスタックトレースを返します。
	 * @return SQLエラーのスタックトレース
	 */
	public StringWriter getSQLStackTrace(){
		return SQLStackTrace;
	}
	
	public long diffSec(String time1,String time2) {
	  int sptime1[] = SplitDate(time1);
	  int sptime2[] = SplitDate(time2);
	  long millisec1 = 0;
	  long millisec2 = 0;
	  long diffsec = 0;
	  
	  Calendar calendar1 = Calendar.getInstance();
	  Calendar calendar2 = Calendar.getInstance();
	  calendar1.clear();
	  calendar2.clear();
	 
	  calendar1.set(sptime1[0],sptime1[1],sptime1[2],sptime1[3],sptime1[4],sptime1[5]);
	  calendar2.set(sptime2[0],sptime2[1],sptime2[2],sptime2[3],sptime2[4],sptime2[5]);
	  
	  millisec1 = calendar1.getTimeInMillis();
	  millisec2 = calendar2.getTimeInMillis();
	  
	  diffsec = millisec2 - millisec1;
	  diffsec = diffsec / 1000;
	  
	  return diffsec;
	}
	
	public int[] SplitDate(String time) {
	  int spdata[] = new int[6];
	  int i;
	  //time = yy/MM/dd HH:mm:ss
	  String yymmddhhmmss[] = time.split(" ");
	  String yymmdd[] = yymmddhhmmss[0].split("/");
	  String hhmmss[] = yymmddhhmmss[1].split(":"); 
	  
	  for (i=0;i<3;i++) {
	    spdata[i] = Integer.parseInt(yymmdd[i]);
	    spdata[i+3] = Integer.parseInt(hhmmss[i]);
	  }
	  
	  return spdata;
	}
	
	/**
	 * ログイン日時を返します。
	 * @return 投稿日時
	 */
	public String getDate(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd/ HH:mm:ss");
		return sdf.format(cal.getTime());
	}
	

}
