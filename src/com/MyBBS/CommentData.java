package com.MyBBS;

/**
 * コメントデータを一時格納するクラスです。
 * @author Ishizuka
 *
 */
public class CommentData {
	public int No;
	public String Name;
	public String Comment;
	public String CommentDate;
	
	/**
	 * コメント番号, 投稿者名, 投稿コメント, 投稿日時をインスタンス生成時に渡して下さい。
	 * @param CommentNo コメント番号
	 * @param CommentName 投稿者名
	 * @param CommentString 投稿コメント
	 * @param CommentDateString 投稿日時
	 */
	CommentData(int CommentNo, String CommentName, String CommentString, String CommentDateString) {
		No = CommentNo;
		Name = CommentName;
		Comment = CommentString;
		CommentDate = CommentDateString;
	}

}
