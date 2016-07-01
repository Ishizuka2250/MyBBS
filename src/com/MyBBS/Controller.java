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
 * �f���N���X���瑗�M���ꂽ�R�����g�f�[�^���f�[�^�x�[�X�Ɋi�[���܂��B
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
	 * @param comment ���e���ꂽ�R�����g
	 * @return ���M��������
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
	 * �f�[�^�x�[�X����R�����g�f�[�^���擾���܂��B
	 * @return �擾��������
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
