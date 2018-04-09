<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	     pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
<title>AlbumEdit</title>
<meta charset="utf-8" />
<meta name="viewport" content="width = 1050, user-scalable = no" />
	<script type="text/javascript" src="../resources/album_page_js/extras/jquery.min.1.7.js"></script>
	<script type="text/javascript" src="../resources/album_page_js/extras/modernizr.2.5.3.min.js"></script>
	<script type="text/javascript" src="../resources/album_page_js/basic.js"></script>		
	
	<script src="../resources/album_drag_and_drop_js/jquery-ui.js"></script>
 	<!-- <script src="../resources/js/jquery-3.3.1.min.js"></script>	 -->
	
	<link rel="stylesheet" href="../resources/album_drag_and_drop_js/jquery-ui.css">		
	<link rel="stylesheet" href="../resources/album_css/album_edit_basic.css">
	<link rel="stylesheet" href="../resources/album_css/album_edit_drag_and_drop.css">

<script>
var count = 0;

function pagePlus(){
	for(var i = 0; i < 2; i++){
		var element = $('<div />');
		element.attr('class' , 'page' + ($('#flipbook').turn('pages')+1));
		element.attr('id' , 'page' + ($('#flipbook').turn('pages')+1));
		alert(element.attr('class'));
		$('#flipbook')
				.turn('addPage',element , $('#flipbook').turn('pages')+1)
			    .turn('pages', $('#flipbook').turn('pages'));
		alert($('#flipbook').turn('pages')+'_pages');
		$('.page'+$('#flipbook').turn('pages')).droppable({
			accept: "#picture_add",
			drop: function(event, ui) {
				var div_holder = document.createElement('div');
				count ++;
				var html = '<a class="close_border"></a> <label for="cross'+count+'"> <input type="file" id="cross'+count+'" class="cross'+$('#flipbook').turn('pages')+'" name="cross'+count+'" onchange="readURL(this)"> </label>';
				
				
				$(div_holder).addClass('holder').html(html);
				$(div_holder).css('position', 'absolute');
				
				$(div_holder).draggable( { containment: '.page-wrapper', scroll: false });
				$(div_holder).resizable();
				
				$(this).append(div_holder);
				
				$('.close_border').on('click', function() {
					$(this).parent().remove();
				});
			}
		});
	}
}

function fileSubmit() {		
		var formData = new FormData();
		for(var i = 0,num=0; i < $('input[class="cross'+$('#flipbook').turn('page')+'"]').size(); i++){
			if($('input[class="cross'+$('#flipbook').turn('page')+'"]')[i].files[0]){
				formData.append('file'+num , $('input[class="cross'+$('#flipbook').turn('page')+'"]')[i].files[0]);
				num++;
			}
		}
		$.ajax({
			url:'albumPageSave',
			processData: false,
			contentType: false,
			type:'POST',		
			data:formData,
			dataType:'text',
			success: function(a){
				if(a=='success'){
					pagePlus();
					$('#flipbook').turn('next');
				}else{
					alert(a);
				}
			},
			error:function(e){alert('파일 업로드 실패');}		
		});
}

$(function() {
	$( '#picture_add' ).draggable({ revert: 'valid' });
	
	$('*').droppable({ //다른 쪽 드롭되도 돌아 올 수 있게 하는 코드
		accept: '#picture_add',
		drop: function(event, ui) {
		}
	});
	
	$('#page1').droppable({
		accept: '#picture_add',
		drop: function(event, ui) {
			var div_holder = document.createElement('div');
			count ++;
			var html = '<a class="close_border"></a> <label for="cross'+count+'"> <input type="file" id="cross'+count+'" class="cross1" name="cross'+count+'" onchange="readURL(this)"> </label>';
			
			
			$(div_holder).addClass('holder').html(html);
			$(div_holder).css('position', 'absolute');
			
			$(div_holder).draggable( { containment: '.page-wrapper', scroll: false });
			$(div_holder).resizable();
			
			$(this).append(div_holder);
			
			$('.close_border').on('click', function() {
				$(this).parent().remove();
			});
		}
	});
});

function readURL(input) {
	
	var target = $(input).parent().parent(); //사진 테두리 div
	
	if (input.files && input.files[0]) {
		$(target).children().hide();
		
		var reader = new FileReader();
		 
		reader.onload = function (e) {
			$(target).append('<a class="close_picture">');
			$(target).css('background-image', "url(" + e.target.result + ")");
			
			
			$('.close_picture').on('click', function() {
	         	$(this).parent().css('background-image', 'url("")');	         	
	         	$(this).parent().children().show();	
	         	$(this).remove();
			});
			
        }

	reader.readAsDataURL(input.files[0]);
    }
}

//앨범 배경 커스텀마이징
 function bgchange(num) {
		
		var bg = num;
		//alert(bg);
		
		if(bg == 0) {
			//alert(bg);
			$('.pages').css("background-image","url(..//resources//image_mj//season.jpg)");
		}
		
		if(bg == 1) {
			//alert(bg);
			$('.pages').css("background-color","pink");
		}
		
		if(bg == 2) {
			//alert(bg);
			$('.pages').css("background-image","url(..//resources//image_mj//vintage.jpg)");
		}
	}
</script>

</head>
<body>


<div id="friend_container">
</div>
   
<!--   앨범제목, 앨범내용, 태그, 댓글, 좋아요, 채팅 -->
<div id="sidebar">	
	<div>	 
       		<form id ="" method="" action="">
			<input type ="text" placeholder = "친구검색"  name="" value = "" class ="search">
			<button class = "bt">s</button>
			</form>					
				<p></p>			
				<p>친구1</p>
				<p>친구2</p>
				<p>친구3</p>
				<p>친구4</p>
		
				<p>그룹1</p>
				<p>그룹2</p>						 		
    	
	</div>			
</div>     

<div id="edit_bar" style="z-index:100; width: 50%;">
	<div id="picture_add" style="background-image: url(../resources/image_mj/photo.png); width : 50px; height: 50px; z-index:99; float:left; width: 10%;"></div>
	<div id="text_add" style="background-image: url(../resources/image_mj/text.png); width : 50px; height: 50px; z-index:99; float:left; width: 10%;"></div>
	<div id="video_add" style="background-image: url(../resources/image_mj/Video-5-icon.png); width : 50px; height: 50px; z-index:99; float:left; width: 10%;"></div>
	
	<div style="width : 50px; height: 50px; z-index:99; float:left; width: 10%;"><input type="button" value="+" id="page_plus" name=""></div>
</div>
<div style="width : 50px; height: 50px; z-index:99; float:left; width: 10%;"><input type="button" value="저장" onClick="fileSubmit();"></div>

<!-- 배경변경버튼 -->
	<button onclick = "bgchange(0)">SAKURA</button>
	<button onclick = "bgchange(1)">PINK</button>
	<button onclick = "bgchange(2)">VINTAGAE</button> 
	
<div id="contents">
	<div class="flipbook-viewport">
		<div class="container">
			<div class="flipbook" id="flipbook">
				<div class="page1" id="page1">
				 	 <input type="file" name="img1" accept="image/*" id="img1"> 
				 </div>
			</div>
		</div>
	</div>
</div>

<!-- 메인표지업로드 -->
 <script type="text/javascript">
 var file1 = document.querySelector('#img1');
	file1.onchange = function () {
	    var fileList = file1.files ;
	    var reader = new FileReader();
	    reader.readAsDataURL(fileList [0]);
	    reader.onload = function  () {
	        document.querySelector('#preview1').src = reader.result ;
	    };
	};	
</script>

<script>

var homePhoto = document.querySelector(".flipbook");

							

homePhoto.onchange = function(e) {

var file = e.target.files[0];

var reader = new FileReader();

reader.addEventListener("load", function() {

var container = e.target.parentNode;

container.style.background = "url("+reader.result+") no-repeat center";container.style["background-size"] = "cover";

}, false);

if (file) {

reader.readAsDataURL(file);

}

}

</script> 
			
<script type="text/javascript">

function loadApp() {

	// Create the flipbook

	$('.flipbook').turn({
			// Width

			width:1200,
			
			// Height

			height:600,

			// Elevation

			elevation: 50,
			
			// Enable gradients

			gradients: true,
			
			// Auto center this flipbook

			autoCenter: true

	});
}



// Load the HTML4 version if there's not CSS transform

yepnope({
	test : Modernizr.csstransforms,
	yep: ['../resources/album_page_js/lib/turn.js'],
	nope: ['../resources/album_page_js/lib/turn.html4.min.js'],
	both: ['../resources/album_css/basic.css'],
	complete: loadApp
});

</script>           

</body>
</html>

  