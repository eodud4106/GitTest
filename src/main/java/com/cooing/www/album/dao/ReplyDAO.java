package com.cooing.www.album.dao;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cooing.www.album.vo.ReplyVO;

@Repository
public class ReplyDAO {
	@Autowired
	SqlSession sqlSession;
	// 댓글 작성
	public void replyWrite(ReplyVO vo) {
		ReplyMapper albumreplymapper = sqlSession.getMapper(ReplyMapper.class);
		albumreplymapper.replyWrite(vo);
	}
	// 댓글 삭제
	public void replyDelete(ReplyVO vo) {
		ReplyMapper albumreplymapper = sqlSession.getMapper(ReplyMapper.class);
		albumreplymapper.replyDelete(vo);	
	}
	// 댓글 목록
	public ArrayList<ReplyVO> listReply(int num, int start, int countPerPage) {
		RowBounds rb = new RowBounds(start, countPerPage);
		ReplyMapper albumreplymapper = sqlSession.getMapper(ReplyMapper.class);
		return albumreplymapper.listReply(num, rb);
	}
	// 댓글 하나 가져옴
	public ReplyVO getReply(int reply_num) {
		ReplyMapper albumreplymapper = sqlSession.getMapper(ReplyMapper.class);
		return albumreplymapper.getReply(reply_num);
	}
	// 댓글 수
	public int getReplyTotal(int num) {
		ReplyMapper albumreplymapper = sqlSession.getMapper(ReplyMapper.class);
		return albumreplymapper.getReplyTotal(num);
	}
}
