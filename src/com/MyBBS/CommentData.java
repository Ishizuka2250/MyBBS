package com.MyBBS;

/**
 * �R�����g�f�[�^���ꎞ�i�[����N���X�ł��B
 * @author Ishizuka
 *
 */
public class CommentData {
	public int No;
	public String Name;
	public String Comment;
	public String CommentDate;
	
	/**
	 * �R�����g�ԍ�, ���e�Җ�, ���e�R�����g, ���e�������C���X�^���X�������ɓn���ĉ������B
	 * @param CommentNo �R�����g�ԍ�
	 * @param CommentName ���e�Җ�
	 * @param CommentString ���e�R�����g
	 * @param CommentDateString ���e����
	 */
	CommentData(int CommentNo, String CommentName, String CommentString, String CommentDateString) {
		No = CommentNo;
		Name = CommentName;
		Comment = CommentString;
		CommentDate = CommentDateString;
	}

}
