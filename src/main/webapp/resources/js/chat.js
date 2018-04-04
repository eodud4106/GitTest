/**
 * 채팅 관련 메소드 모음
 */
var websocket;
var websocket_alert;
var wsUri = "ws://localhost:8080/www/chat/echo.do";

$(document).ready(function () {
 	$("#sendBtn").on('click', function() {
		sendMessage();
	});
 	
 	$('#inputbutton_close').on('click', function() {
 		closePChat();
 	});

 	websocket_alert2 = new WebSocket(wsUri);
 	
})

function closePChat(evt) {
	
	$('#div_chat').css('display', 'none');
	websocket.close();
}

function openPChat(id, friend_id, goRoot) {
	
	if ($('#div_chat').css('display') == 'block') {
		closePChat();
	}
	
	sessionStorage.setItem('id', id);
	sessionStorage.setItem('friend_id', friend_id);
	
    websocket = new WebSocket(wsUri);
    
   	websocket.onmessage = function(evt) {
   		onMessage(evt)
   	};
   	websocket.onopen = function(evt) {
   		onOpen(evt)
   	};
   	websocket.onerror = function(evt) {
   		onError(evt)
   	};
   	websocket.onclose = function(evt) {
   		closePChat(evt)
   	};
		
	$.ajax({
		url: goRoot + 'chat/getChat',
		type: 'post',
		data: {
			friend_id: friend_id
		},
		dataType: 'text',
		success: function(result) {
			printChat(result);
		},
		error: function(e) {
			alert('오류 발생, 코드 ->' + e.status);
		}
	});
	
	$('#div_chat').css('display', 'block');

}

function printChat(result) {
	
	var chatData = JSON.parse(result);
	$(chatData).each(function(i, chat) {
		
		var div_message = document.createElement('div');
		if (chat.p_message_from == sessionStorage.getItem('id')) {
			//자기가 보낸 메시지
			$(div_message).css('text-align', 'right').html("<span tag='read' style='font-size: 6pt' >1 </span>" + 
					chat.p_message_date + " / " + chat.p_message_message + " < <br>");
		} else {
			//받은 메시지
			$(div_message).css('text-align', 'left').html(chat.p_message_from + " > " + chat.p_message_message + " / " + chat.p_message_date + "<br>");
		}
		
		$("#data").append(div_message);
		
	});
	
	$("#data").scrollTop($("#data")[0].scrollHeight);
	
}


/* 메시지 발신 */
function sendMessage() {
	
	//TODO 수신자, 발신자를 잘....
    var sendMessage = {
			type: "message",
			from: sessionStorage.getItem('id'),
			to: sessionStorage.getItem('friend_id'),
			message: $('#message').val()
		}

    websocket.send(JSON.stringify(sendMessage));
    $('#message').val('').focus();

}

function onOpen(evt) {
	//TODO 채팅창 열렸을 때 모든 메시지를 핸들러가 보낸다. 여기서는 그 메시지를 차곡차곡 잘 보여주자...
	//open할 때... 현재 유저의 아이디를 서버로 보내자...
	var sendMessage = {
		type: "loginId",
		id: sessionStorage.getItem('id')
	}

	websocket.send(JSON.stringify(sendMessage));
	
	readMessage();
}

//서버로부터 메시지를 받으면 호출됨.
function onMessage(evt) {

    var data = evt.data;
    
    var chatData = JSON.parse(data);
    
    if (chatData.type != null && chatData.type == 'read') {
		$('span[tag="read"]').remove();
		return;
	}
    
    var from = chatData.p_message_from;
    var message = chatData.p_message_message;
    var m_date = chatData.p_message_date.substring(0,16);
		//일대일 채팅이므로.. 메세지를 수신받았다면 당연히 읽은 것으로 본다.. 따라서
    
    var div_message = document.createElement('div');
    
    if (from == sessionStorage.getItem('id')) {
		//자기가 보낸 메시지
		$(div_message).css('text-align', 'right').html("<span tag='read' style='font-size: 6pt' >1 </span>" + 
				m_date + " / " + message + " < <br>");
	} else {
		//다른 사람이 보낸 메시지
		$(div_message).css('text-align', 'left').html(from + " > " + message + " / " + m_date + "<br>");
		readMessage();
	}
    
    $("#data").append(div_message);
    
    $("#data").scrollTop($("#data")[0].scrollHeight);
   
}


function onClose(evt) {

    $("#data").append("연결 끊김");

}

function readMessage() {
    //메시지를 수신 받았다면.. 채팅방에 접속한 상태이고 따라서 메시지를 읽은 것으로 본다..
    var sendMessage = {
		type: "read",
		to: sessionStorage.getItem('id'),
		from: sessionStorage.getItem('friend_id')
	}
   	websocket.send(JSON.stringify(sendMessage));
}

function setTo() {
		
}