package com.cooing.www.album.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;

import com.cooing.www.album.vo.AlbumReplyVO;

public interface AlbumReplyMapper {
	// 댓글 작성
	public void replyWrite(AlbumReplyVO vo);
	// 댓글 삭제
	public void replyDelete(AlbumReplyVO vo);
	// 댓글 목록
	public ArrayList<AlbumReplyVO> listReply(int num, RowBounds rb);
	// 댓글 하나 가져옴
	public AlbumReplyVO getReply(int reply_num);
	// 댓글 수
	public int getReplyTotal(int num);
	
}