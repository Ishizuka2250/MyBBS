package com.MyBBS;

import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.MyBBS.CommentData;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 掲示板クラスから送信されたコメントデータをデータベースに格納します。
 * @author Ishizuka
 * 
 */
public class BBSController {
	//private static String Sqlite = "jdbc:sqlite:C:/Users/Ishizuka/Documents/GitHub/MyBBS/sqlite/BBS.sqlite3";
	private static String SqlitePath = "C:/Sqlite/BBS.sqlite3";
	private static StringWriter SQLStackTrace = new StringWriter();
	private static PrintWriter pw = new PrintWriter(SQLStackTrace);
	private static ArrayList<CommentData> CommentDataList = new ArrayList<CommentData>();
	
	/**
	 * C:\Sqlite\ に掲示板データベースがない場合{@link #initSqlite()}を呼び出します。
	 * @throws ClassNotFoundException
	 */
	public BBSController() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		File existSqlite = new File(SqlitePath);
		File existSqliteDir = new File("C:/Sqlite");
		
		TableCheck();
		
		//C:\Sqlite\ に掲示板データベースがない場合自動作成します。
		if (existSqlite.exists() == false) {
			if(existSqliteDir.exists() == false) existSqliteDir.mkdir();
			try{
				FileWriter fw = new FileWriter(SqlitePath,false);
				fw.close();
			}catch (IOException e){
				e.printStackTrace(pw);
				pw.flush();
				return;
			}
			initSqlite();
		}
		
	}
	
	/**
	 * C:\Sqlite\ に掲示板のデータベース(BBS.sqlite)を作成します。
	 * @return 初期化成功判定
	 */
	public boolean initSqlite() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
            statement.execute("create table CommentData(No int,Name text,Date text,Comment text);");
            statement.execute("create table LoginUser(UserName text,LoginID text,Password text);"); 
			
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
	 * 以下のテーブルがDB上に存在するかチェックします。<br>
	 * - CommentData (掲示板コメント管理テーブル)<br>
	 * - LoginUser (ユーザーID/パスワード管理テーブル)
	 */
	private void TableCheck() {
		boolean CommentDataTable_exist = false;
		boolean LoginUserTable_exist = false;
		
		try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
            
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            
            ResultSet result = statement.executeQuery("select * from sqlite_master");
            while(result.next()){
                if (result.getString(2).equals("CommentData")) CommentDataTable_exist = true;
                if (result.getString(2).equals("LoginUser")) LoginUserTable_exist = true;
            }
            
            if (! CommentDataTable_exist) statement.execute("create table CommentData(No int,Name text,Date text,Comment text);");
            if (! LoginUserTable_exist) statement.execute("create table LoginUser(UserName text,LoginID text,Password text);"); 
		
            statement.close();
            connection.close();
		}catch (SQLException e){
			e.printStackTrace(pw);
			pw.flush();
		}
	}
	
	
	/**
	 * 掲示板に投稿されたコメント数を取得します。
	 * @return 投稿されたコメント数
	 */
	public int updateCommentNo() {
		int no;
		try{
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			ResultSet result = statement.executeQuery("select count(*) from CommentData");
			result.next();
			no = result.getInt(1);
			
			statement.close();
			connection.close();

			return no;
		}catch(SQLException e) {
			e.printStackTrace(pw);
			pw.flush();
		}
		return -1;
	}
	
	/**
	 * 掲示板に投稿されたコメントをデータベースへ送信します。
	 * @param name 投稿者名
	 * @param comment 投稿されたコメント
	 * @return 送信成功判定
	 */
	public boolean commit(String name,String comment) {
		int No = updateCommentNo();
		
		if (comment == "") return true;
		if (name == "") name = "NoName";
		
		
		CommentData commentdata = new CommentData(No+1,name,comment,this.getDate());
		
		try{
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);

			String sql = "insert into CommentData values(" + commentdata.No + ",\'" + commentdata.Name + "\',\'" + commentdata.CommentDate + "\',\'" + commentdata.Comment + "\');";
			
			statement.execute(sql);
			
			statement.close();
			connection.close();
		} catch (SQLException e){
			e.printStackTrace(pw);
			pw.flush();
			return false;
		}
		
		return true;
	}
	
	/**
	 * データベースからコメントデータを取得します。
	 * @return 取得成功判定
	 */
	public boolean getCommentData() {
		CommentDataList.clear();
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			ResultSet result = statement.executeQuery("select * from CommentData");
			
			while(result.next()) {
				CommentData commentdata = new CommentData(result.getInt("No"),result.getString("Name"),result.getString("Comment"),result.getString("Date"));
				CommentDataList.add(commentdata);
			}
			
			statement.close();
			connection.close();
		}catch (SQLException e){
			e.printStackTrace(pw);
			pw.flush();
			return false;
		}
		return true;
	}
	
	public ArrayList<CommentData> getCommentDataList() {
		return CommentDataList;
	}
	
	/**
	 * 投稿日時を返します。
	 * @return 投稿日時
	 */
	public String getDate(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat();
		return sdf.format(cal.getTime());
	}
	
	/**
	 * データベース処理クラスで発生したSQLエラーのスタックトレースを返します。
	 * @return SQLエラーのスタックトレース
	 */
	public StringWriter getSQLStackTrace(){
		return SQLStackTrace;
	}
	
}
