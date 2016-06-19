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
 * �f���N���X���瑗�M���ꂽ�R�����g�f�[�^���f�[�^�x�[�X�Ɋi�[���܂��B
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
	 * C:\Sqlite\ �Ɍf���f�[�^�x�[�X���Ȃ��ꍇ{@link #initSqlite()}���Ăяo���܂��B
	 * @throws ClassNotFoundException
	 */
	public Controller() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		File existSqlite = new File(SqlitePath);
		File existSqliteDir = new File("C:/Sqlite");
		
		//C:\Sqlite\ �Ɍf���f�[�^�x�[�X���Ȃ��ꍇ�����쐬���܂��B
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
	 * C:\Sqlite\ �Ɍf���̃f�[�^�x�[�X(BBS.sqlite)���쐬���܂��B
	 * @return ��������������
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
	 * �f���ɓ��e���ꂽ�R�����g�����擾���܂��B
	 * @return ���e���ꂽ�R�����g��
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
	 * �f���ɓ��e���ꂽ�R�����g���f�[�^�x�[�X�֑��M���܂��B
	 * @param name ���e�Җ�
	 * @param date ���e���ꂽ���t
	 * @param comment ���e���ꂽ�R�����g
	 * @return ���M��������
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
	 * �f�[�^�x�[�X����R�����g�f�[�^���擾���܂��B
	 * @return �擾��������
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
	 * �R�����gNo�̃��X�g��Ԃ��܂��B
	 * @return �R�����gNo�̃��X�g
	 */
	public ArrayList<Integer> getNoList() {
		return NoList;
	}
	
	/**
	 * ���e�҂̃��X�g��Ԃ��܂��B
	 * @return ���e�҃��X�g
	 */
	public ArrayList<String> getNameList() {
		return NameList;
	}
	
	/**
	 * ���e�����̃��X�g��Ԃ��܂��B
	 * @return ���e�������X�g
	 */
	public ArrayList<String> getDateList() {
		return DateList;
	}
	
	/**
	 * �R�����g���X�g��Ԃ��܂��B
	 * @return �R�����g���X�g
	 */
	public ArrayList<String> getCommentList() {
		return CommentList;
	}
	
	/**
	 * ���e������Ԃ��܂��B
	 * @return ���e����
	 */
	public String getDate(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat();
		return sdf.format(cal.getTime());
	}
	
	/**
	 * �f�[�^�x�[�X�����N���X�Ŕ�������SQL�G���[�̃X�^�b�N�g���[�X��Ԃ��܂��B
	 * @return SQL�G���[�̃X�^�b�N�g���[�X
	 */
	public StringWriter getSQLStackTrace(){
		return SQLStackTrace;
	}
	
	
}
