package com.MyBBS;

import java.io.*;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	private static ArrayList<Integer> NoList = new ArrayList<Integer>();
	private static ArrayList<String> NameList = new ArrayList<String>();
	private static ArrayList<String> CommentList = new ArrayList<String>();
	private static ArrayList<String> DateList = new ArrayList<String>();
	
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
	 * @param date 投稿された日付
	 * @param comment 投稿されたコメント
	 * @return 送信成功判定
	 */
	public boolean commit(String name,String date,String comment) {
		int id;
		id = updateCommentNo();
		if (name == "") name = "NoName";
		
		try{
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);

			String sql = "insert into CommentData values(" + (id+1) + ",\'" + name + "\',\'" + date + "\',\'" + comment + "\');";
			
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
		NoList.clear();
		NameList.clear();
		DateList.clear();
		CommentList.clear();
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			ResultSet result = statement.executeQuery("select * from CommentData");
			
			while(result.next()) {
				NoList.add(result.getInt("No"));
				NameList.add(result.getString("Name"));
				DateList.add(result.getString("Date"));
				CommentList.add(result.getString("Comment"));
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
	
	/**
	 * コメントNoのリストを返します。
	 * @return コメントNoのリスト
	 */
	public ArrayList<Integer> getNoList() {
		return NoList;
	}
	
	/**
	 * 投稿者のリストを返します。
	 * @return 投稿者リスト
	 */
	public ArrayList<String> getNameList() {
		return NameList;
	}
	
	/**
	 * 投稿日時のリストを返します。
	 * @return 投稿日時リスト
	 */
	public ArrayList<String> getDateList() {
		return DateList;
	}
	
	/**
	 * コメントリストを返します。
	 * @return コメントリスト
	 */
	public ArrayList<String> getCommentList() {
		return CommentList;
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
