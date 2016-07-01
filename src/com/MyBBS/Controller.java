package com.MyBBS;

import java.io.*;
import java.util.*;
import org.apache.commons.lang3.RandomStringUtils;
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
public class Controller {
	//private static String Sqlite = "jdbc:sqlite:C:/Users/Ishizuka/Documents/GitHub/MyBBS/sqlite/BBS.sqlite3";
	private static String SqlitePath = "C:/Sqlite/BBS.sqlite3";
	private static StringWriter SQLStackTrace = new StringWriter();
	private static PrintWriter pw = new PrintWriter(SQLStackTrace);
	private static ArrayList<CommentData> CommentDataList = new ArrayList<CommentData>();
	private static String token;
	
	/**
	 * C:\Sqlite\ に掲示板データベースがない場合{@link #initSqlite()}を呼び出します。
	 * @throws ClassNotFoundException
	 */
	public Controller() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		File existSqlite = new File(SqlitePath);
		File existSqliteDir = new File("C:/Sqlite");
		
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
