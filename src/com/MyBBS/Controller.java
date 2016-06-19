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

public class Controller {
	//private static String Sqlite = "jdbc:sqlite:C:/Users/Ishizuka/Documents/GitHub/MyBBS/sqlite/BBS.sqlite3";
	private static String SqlitePath = "C:/Sqlite/BBS.sqlite3";
	private static StringWriter SQLStackTrace = new StringWriter();
	private static PrintWriter pw = new PrintWriter(SQLStackTrace);
	private static int LastCommentNo;
	private static ArrayList<Integer> IDList = new ArrayList<Integer>();
	private static ArrayList<String> NameList = new ArrayList<String>();
	private static ArrayList<String> CommentList = new ArrayList<String>();
	private static ArrayList<String> DateList = new ArrayList<String>();
	
	public Controller() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		File existSqlite = new File(SqlitePath);
		File existSqliteDir = new File("C:/Sqlite");
		
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
		
		LastCommentNo = updateCommentNo();
	}
	
	public void initSqlite() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + SqlitePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			statement.executeQuery("create table CommentData(No int,Name text,Date text,Comment text)");
			
			statement.close();
			connection.close();
		}catch (SQLException e) {
			e.printStackTrace(pw);
			pw.flush();
			return;
		}
		return;
	}
	
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
	
	public int getLastCommentNo() {
		return LastCommentNo;
	}
	
	public boolean commit(String name,String date,String comment) {
		int id;
		id = updateCommentNo();
		if (name == "") name = "NoName";
		
		try{
			Connection connection = DriverManager.getConnection(SqlitePath);
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
	
	public boolean getCommentData() {
		IDList.clear();
		NameList.clear();
		DateList.clear();
		CommentList.clear();
		
		try {
			Connection connection = DriverManager.getConnection(SqlitePath);
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);
			
			ResultSet result = statement.executeQuery("select * from CommentData");
			
			while(result.next()) {
				IDList.add(result.getInt("No"));
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
	
	
	public ArrayList<Integer> getIDList() {
		return IDList;
	}
	
	public ArrayList<String> getNameList() {
		return NameList;
	}
	
	public ArrayList<String> getDateList() {
		return DateList;
	}
	
	public ArrayList<String> getCommentList() {
		return CommentList;
	}
	
	public String getDate(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat();
		return sdf.format(cal.getTime());
	}
	
	public StringWriter getSQLStackTrace(){
		return SQLStackTrace;
	}
	
	
}
