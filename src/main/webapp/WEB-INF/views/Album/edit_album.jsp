<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>

<html>
<head>
<title>AlbumEdit</title>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

<!-- 기본 js -->
<script type="text/javascript" src="../resources/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="../resources/js/jquery-ui.min.js"></script>
<script defer src="https://use.fontawesome.com/releases/v5.0.10/js/all.js"></script>

<script src="../resources/aside_js/popper.min.js"></script>
<script src="../resources/aside_js/owl.carousel.min.js"></script>
<script src="../resources/aside_js/jquery.waypoints.min.js"></script>
<script src="../resources/aside_js/imagesloaded.pkgd.min.js"></script>
<script src="../resources/aside_js/main.js"></script>

<script src="../resources/skin_radio/icheck.js"></script>

<!-- 페이지 넘김 효과를 위한 js -->
<script type="text/javascript" src="../resources/js/turn.js"></script>

<!-- albumEdit 용 js -->
<script type="text/javascript" src="../resources/js/albumEdit.js"></script>

<!-- 기본 css -->
<link rel="stylesheet" href="../resources/css/albumEdit.css">
<link rel="stylesheet" href="../resources/css/jquery-ui.min.css">

<!-- 기타 css -->
<link rel="stylesheet" href="../resources/album_css/album_edit_basic.css">
<link rel="stylesheet" href="../resources/album_css/album_edit_drag_and_drop.css">

<link rel="stylesheet" href="../resources/aside_css/bootstrap.min.css">
<link rel="stylesheet" href="../resources/aside_css/open-iconic-bootstrap.min.css">
<link rel="stylesheet" href="../resources/aside_css/owl.carousel.min.css">
<link rel="stylesheet" href="../resources/aside_css/owl.theme.default.min.css">
<link rel="stylesheet" href="../resources/aside_css/icomoon.css">
<link rel="stylesheet" href="../resources/aside_css/animate.css">
<link rel="stylesheet" href="../resources/aside_css/style.css">

<link rel="stylesheet" href="../resources/skin_radio/green.css">

<style type="text/css">

.main {
    min-width: 200px;
    max-width: 200px;
    padding: 10px;
    margin: 0 auto;
    background: #ffffff;}
section {
    display: none;
    padding: 20px 0 0;    
    font-size : 14px;        
    border-top: 1px solid #ddd;}
    
/*라디오버튼 숨김*/
.input1 {
      display: none;}

label {
    display: inline-block;
    margin: 0 0 -1px;
    padding: 5px 10px;
    font-weight: 600;
    text-align: center;
    color: #bbb;
    border: 1px solid transparent;
    font-size: 15px;}

label:hover {
    color: #2e9cdf;
    cursor: pointer;}

/*input 클릭시, label 스타일*/
.input1:checked + label {
      color: #555;
      border: 1px solid #ddd;
      border-top: 2px solid #2e9cdf;
      border-bottom: 1px solid #ffffff;}

#tab1:checked ~ #content1,
#tab2:checked ~ #content2{
    display: block;}

.search{	
	width: 120px;
	display:block;
	position: absolute;	
}
.bt{
	position: absolute;
	right: 40px;
}
.tb1{
	padding-top: 20px;
}	    

html, body, main, .container-fluid {
	height: 100%;
}
.container-fluid {
	padding: 0;
}

.view_wrapper {
	margin: 0;
	margin-left: 250px;
	display: flex;
	flex-wrap: wrap;
}
.album_wrapper, .top_bar {
	margin: auto !important;
	display: block;
}
.checkbox {
	font-size: 20px;
}
.page {
	background-color: #eee;
}
.outer {
	background-color: #aaa;
}
</style>

<script>
	
	var selectcheck = true;


	//라디오버튼
	$(document).ready(function() {

		$('.input').iCheck({
			radioClass : 'iradio_square-green',
		// increaseArea: '20%' // optional

		});

		//value값
		
		alert(JSON.stringify('${album}'));
		alert(JSON.stringify('${arr_page}'));

	});

	function checkRadioButton(iCheck){   
	   
	   var temp;
	   
	   var radioObj = document.all(iCheck);
	   
	   
	   var isChecked;
	   if(radioObj.length == null)
	   { // 라디오버튼이 같은 name 으로 하나밖에 없다면
	   isChecked = radioObj.checked;
	   }
	   else
	   { // 라디오 버튼이 같은 name 으로 여러개 있다면
	      for(var i=0; i<radioObj.length; i++)
	      {
	         if(radioObj[i].checked)
	         {
	            isChecked = true;
	            break;
	         }
	      }
	   }

	   if(isChecked){
		   alert('체크된거있음' + radioObj[i].value);
		   temp = radioObj[i].value; 
		   alert('템프 값 : ' + temp);
		   
		   //value값
		   switch (temp) {
		      case '1':
		         alert(temp);
		         $('.pages').css("background-image","url(..//resources//image_mj//season.jpg)"); 
		         break;
		         
		      case '2':
		         alert(temp);
		         $('.pages').css("background-color","pink");
		         break;
	
		      default:
		         alert(temp);
		         $('.pages').css("background-image","url(..//resources//image_mj//vintage.jpg)");
		         break;    
		   }
	   
	   }else{
			alert('체크된거없음');
	   }	 
	}

</script>

</head>
<body>

	<!-- 사이드 바 -->
	<aside class="probootstrap-aside js-probootstrap-aside">
		<a href="#" class="probootstrap-close-menu js-probootstrap-close-menu d-md-none">
			<span class="oi oi-arrow-left"></span> Close
		</a>
		<div class="probootstrap-site-logo probootstrap-animate" data-animate-effect="fadeInLeft">
			<a href="index.html" class="mb-2 d-block probootstrap-logo">COOING</a>
			<p class="mb-0"> 친구목록출력, 채팅기능
				<a href="https://uicookies.com/" target="_blank">uiCookies</a>
			</p>
		</div>
		<div class="probootstrap-overflow">
		<div class="main">
		<input class = "input1" id="tab1" type="radio" name="tabs" checked> <!--디폴트 메뉴-->
		<label for="tab1">앨범생성</label>

  		<input class = "input1" id="tab2" type="radio" name="tabs">
    	<label for="tab2">채팅</label>   

    	<section id="content1"> 
    	<!-- 페이지 저장 -->		
			<div id="entry">
				<h5 style="color: black;">앨범명</h5><input type="text" id="album_name" name="album_name" value="앨범이름을 입력해주세요.">
				<h5 style="color: black; ">앨범 내용</h5>
				<input type="text" style = "height: 100px;"id="album_contents" name="album_contents" value="앨범내용을 입력해주세요.">
				<h5 style="color: black;">앨범 카테고리</h5>
				<select name="album_category" id="album_category">		
					<option value="0">여행</option>
				    <option value="1">스포츠/래저</option>
				    <option value="2">동물</option>
				    <option value="3">음악</option>
				    <option value="4">요리/음식</option>
				    <option value="5">패션/뷰티</option>
				    <option value="6">연예/TV</option>
				    <option value="7">게임</option>
				    <option value="8">영화</option>
				    <option value="9">도서</option>
				    <option value="10">공연/전시</option>
				    <option value="11">외국어</option>
				    <option value="12">전문지식</option>
				    <option value="13">수집/제작</option>
				    <option value="14">자기계발</option>
				    <option value="15">육아</option>
				    <option value="16">일상생활</option>
				    <option value="17">자동차</option>
				    <option value="18">낚시</option>
				    <option value="19">건강</option>
				    <option value="20" selected="selected">기타</option>
				</select>
				<h5 style="color: black;">앨범 공개범위</h5>
				<select name="album_openrange" id="album_openrange">		
					<option value="1" selected="selected">나만 보기</option>
				    <option value="2">전체 공개</option>
				    <option value="3">더 추가해서 ㄱㄱ</option>
				</select>
				
				<input type="button" value="앨범정보저장" onclick="modifiy_AlbumInfomation()">
				<br><br><br>
				<!-- <input type="text" id="hashtagtx" placeholder="해쉬태그"><input type="button" id="hashtagbt" value="추가">--> 
				<div id="hashtagvw"></div>
				<br><br>
				<input type="button" id="hashtagbt" value="추가">
				<input type="hidden" name="album_num" value="${albumnum }" id="album_num">
				
				<br><br>
				<!-- <input type="hidden" id="hashtag" name="hashtag"> -->
				<!-- <input type="submit" onsubmit="formCheck()"> -->
			</div>
		    
       
    	</section>

   		<section id="content2">
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
				<form id="testimg">
					<input type="hidden" name="imgSrc" id="imgSrc" />
				</form>	
    	</section>
    	</div>			

			<footer class="probootstrap-aside-footer probootstrap-animate" data-animate-effect="fadeInLeft">

				<p>
					&copy; 2018 <a href="https://uicookies.com/" target="_blank">COOING</a>
					<br> All Rights Reserved.
				</p>
			</footer>
		</div>
	</aside>

	<!-- 메인 -->
	<main role="main" class="probootstrap-main2 js-probootstrap-main">
		<div class="probootstrap-bar">
	
			<a href="#" class="probootstrap-toggle js-probootstrap-toggle">
				<span class="oi oi-menu"></span>
			</a>
			<div class="probootstrap-main-site-logo">
				<a href="index.html">COOING</a>
			</div>
	
		</div>
	
		<div class="container-fluid">
			<div class="view_wrapper">
	
				<div class="col-xl-8 col-lg-12 top_bar">
					<!-- 텍스트, 이미지 삽입 버튼 -->
					<div class="tool text"><i class="fas fa-align-justify"></i></div>
                	<div class="tool image"><i class="far fa-image"></i></div>
                	
					<!-- 배경변경버튼 -->
					<form name="form" style = "float:right;">
						<input type="radio" name="iCheck" class = "input"value="1" >Sakura
						<input type="radio" name="iCheck" class = "input"value="2" >Pink
						<input type="radio" name="iCheck" class = "input"value="3" checked>Vintage
						<div style= "z-index:99; float:right; " onClick="checkRadioButton('iCheck')"><i class="far fa-check-circle"></i></div>
					</form>
	
				</div>
				
				<!-- 앨범 영역 -->
				<div class="album_wrapper" id="album_wrapper">
					<div class="album" id="album"></div>
				</div>

				<!-- 하단 바 영역 -->
				<div class="under_bar">
					<button>이전</button>
					<button onclick="savePage()">저장</button>
					<button onclick="addPage()">페이지 추가</button>
					<button onclick="removePage()">페이지 삭제</button>
					<button>다음</button>
					<input type="hidden" id="hidden_album_num" value="${album.album_num}">
				</div>
				
			</div>
			<!-- END row -->
	
		</div>

	</main>

</body>
</html>