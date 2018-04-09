package com.cooing.www.hindoong.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.cooing.www.hindoong.vo.MessageVO;

public interface MessageMapper {

	public int insertMessage(MessageVO message);
	
	//해당 메세지의 읽음 숫자를 하나 줄인다.
	public int updateMessage(HashMap<String, String> map);
	
	public ArrayList<MessageVO> selectMessage(HashMap<String, String> map);
	
}