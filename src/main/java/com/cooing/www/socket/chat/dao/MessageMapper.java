package com.cooing.www.socket.chat.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.cooing.www.socket.chat.vo.MessageVO;

public interface MessageMapper {

	public int insertMessage(MessageVO message);
	
	//해당 메세지의 읽음 숫자를 하나 줄인다.
	public int updateMessage(MessageVO message);
	
	public ArrayList<MessageVO> selectMessage(HashMap<String, String> map);
	
	public ArrayList<MessageVO> select_unread_message(HashMap<String, String> map); 
	
}
