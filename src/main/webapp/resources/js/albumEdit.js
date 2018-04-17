// [start] 맵을 쓰기 위한 코드
Map = function(){
    this.map = new Object();
};   
Map.prototype = {   
    put : function(key, value){   
        this.map[key] = value;
    },   
    get : function(key){   
        return this.map[key];
    },
    containsKey : function(key){    
        return key in this.map;
    },
    containsValue : function(value){    
        for(var prop in this.map){
            if(this.map[prop] == value) return true;
        }
        return false;
    },
    isEmpty : function(key){    
        return (this.size() == 0);
    },
    clear : function(){   
        for(var prop in this.map){
            delete this.map[prop];
        }
    },
    remove : function(key){    
        delete this.map[key];
    },
    keys : function(){   
        var keys = new Array();   
        for(var prop in this.map){   
            keys.push(prop);
        }   
        return keys;
    },
    values : function(){   
        var values = new Array();   
        for(var prop in this.map){   
            values.push(this.map[prop]);
        }   
        return values;
    },
    size : function(){
        var count = 0;
        for (var prop in this.map) {
            count++;
        }
        return count;
    }
};
// [end] 맵을 쓰기 위한 코드


// 전역 변수 선언
var map_box = new Map();    //box를 담을 map
var count = 0;              //각 텍스트박스에 id를 주기 위해 증가시킬 변수
var arr_id_of_div_box = [];      //div_box의 id를 담을 jquery 배열

// [start] 페이지 로딩 후 처리
$(document).ready(function(){

    //캔버스 변수 선언, 엘리멘트 연결
    var page = $(".pages");

    // .tool을 드래그할 경우 드래그한 element를 복제한 helper 생성
    // (캔버스 위에 생성되는 textbox는 helper 속성을 물려받는다.)
    $(".tool").draggable({
        helper: "clone"
    });

    // 캔버스에 아이템 드랍 시 이벤트 처리
   page.droppable({
        drop: function(event, ui) {

            // node는 각 텍스트 상자, node를 상기 기본값으로 초기화한다.
            var id = 'box_' + count;

            // node 객체 생성
            var node = {
                id: id,
                position: ui.helper.position(),
                width: ui.helper.width,
                height: ui.helper.height
            };

            // node의 최초 위치 조정..?
            node.position.left -=page.position().left;

            // 드랍한 아이템이 3가지 텍스트 메뉴 중 어느 것인지 판별해서 type에 저장
            if(ui.helper.hasClass("text")){
                node.type = "text";
            } else if(ui.helper.hasClass("image")){
                node.type = "image";
            } else if(ui.helper.hasClass("video")){
                node.type = "video";
            } else {
                return;
            }

            // node를 map에 저장
            map_box.put(id, node);

            // 드랍으로 만든 node를page 위에 그림
            renderbox(node);

            // node의 아이디를 구별하기 위해 1 증가
            count++;
        }
    });

    // 편집창이 아닌 곳 클릭하면.... 효과 해제해버림...
    $('body').mousedown(function(e) {
        e.stopPropagation();

        $('#title').text(e.target.nodeName);

        if(!$(e.target.parentNode.parentNode).hasClass('edit')) {

            // 편집창 제거
            removeEdit();

            // onEdit, onSelect 상태인 박스가 있다면 클래스 삭제, 효과 초기화, z-index 조정
            $('.onEdit').draggable('enable').resizable('disable').prop("contenteditable", false).css({
                    "z-index": 2 + arr_id_of_div_box.indexOf($('.onEdit').attr('id'))
                }).removeClass('onEdit').find(".ui-resizable-handle").hide();
            $('.onSelect').draggable('enable').resizable('disable').css({
                    "z-index": 2 + arr_id_of_div_box.indexOf($('.onSelect').attr('id'))
                }).removeClass('onSelect').find(".ui-resizable-handle").hide();
        }
    })

    // 백스페이스 누를 시 onSelect 박스 삭제!!!!
    $('body').keydown(function(e){
        // 백스페이스 keyCode == 8
        if(e.keyCode == '8') {

            // onSelect인 박스일 경우만 삭제
            if($('.onSelect')) {

                // 편집창 제거
                removeEdit();

                // arr_id_of_div_box 조정
                var id_index = arr_id_of_div_box.indexOf($('.onSelect').attr('id'));
                arr_id_of_div_box.splice(id_index, 1);

                // arr_id_of_div_box를 바탕으로 모든 박스의 z-index 조정
                for(var i = 0; i < arr_id_of_div_box.length; i++) {
                    $('#' + arr_id_of_div_box[i] + '').css({
                        "z-index": 2 + i
                    })
                }

                // onSelect 박스 삭제
                $('.onSelect').remove();
            }
        }
    })

});
// [end] 페이지 로딩 후 처리

//TODOpage 초기화
function initpage(diagram) {
   page.empty();
}

// [start] 텍스트 박스를 캔버스에 추가
function renderbox(node) {

    // var div_box;

    // if(node.id == null) {
    //     // 새로 만든 박스
    // } else {

    // }

    // 위치, 크기 조절 이벤트 적용할 div_box
    // box 드래그, 리사이즈 초기화
    var $div_box = $('<div />', {
        'id' : node.id,
        'class' : 'div_box'
    });

    // node의 타입별 textfield 기본 폰트 크기 지정
    switch(node.type) {
        case "text": 
            $div_box.addClass('textbox').text('입력하세요.....!');
            break;
        case "image":
            var $init_image = $('<img />', {
                src : 'https://vignette.wikia.nocookie.net/wikiaglobal/images/'
                    + '4/4c/Wikia-Visualization-Main%2Chamsters.png/revision/latest?cb=20130314175520'
            }).css({
                "width": "100%",
                "height": "100%"
            })
            $div_box.addClass('imagebox').append($init_image);
            break;
        case "video": $div_box.addClass('videobox'); break;
    }

    $div_box.css({
        "position": "absolute",
        "top": node.position.top,
        "left": node.position.left,
        "width": node.width,
        "height": node.height
    }).draggable({
        // textbox 드래그 시 위치 이동 처리 (ui.helper는 이벤트의 대상)
        stop: function(event, ui) {
            var id = ui.helper.attr("id");
            var box = map_box.get(id);
            box.position.top = ui.position.top;
            box.position.left = ui.position.left;
        },
        drag: function(event, ui) {
            $('.div_whole_editor').css({
                "top": ui.position.top + $('.pages').position().top + 40,
                "left": ui.position.left + $('.pages').position().left
            });
        },
        containment: $('.pages')  // 캔버스 영역 밖으로 나가지 못하게 제한
    }).resizable({
        // textbox 크기 조절 처리
        stop: function(event, ui) {
            var id = ui.helper.attr("id");
            var box = map_box.get(id);
            box.width = $(this).width();
            box.height = $(this).height();
        },
        containment: $('.pages'), // 캔버스 영역을 넘지 못하도록 제한
        disabled: true          // 리사이즈는 onSelect 상태인 박스만 가능하므로.. 초기 설정에는 disable
    });

    //page에 box 출력
    $div_box.find(".ui-resizable-handle").hide();
    $('.pages').append($div_box);
    arr_id_of_div_box.push($div_box.attr('id'));
    $div_box.css({
        "z-index": 2 + arr_id_of_div_box.indexOf($div_box.attr('id'))
    })
    //$('#selection').text(arr_id_of_div_box);

    // textbox 마우스 다운 시 크기 조절 모드 + 전역 편집 모드
    $div_box.mousedown(function(e) {
        //이벤트 bubble 제거
        e.stopPropagation();

        // 클릭한 대상이...
        if($(this).hasClass('onEdit')) {
            // 뭐하지?????
        } else if ($(this).hasClass('onSelect')) {
            // 선택 중인 박스였다면... 무얼 할까??
        } else {
            // 아무 것도 아닌 박스였다면... 수정 중/선택 중인 다른 박스 모두 해제하고... 클릭한 대상에게 선택 중 효과 적용..
            $('.onEdit').draggable('enable').resizable('disable').prop("contenteditable", false).css({
                    "z-index": 2 + arr_id_of_div_box.indexOf($('.onEdit').attr('id'))
                }).removeClass('onEdit').find(".ui-resizable-handle").hide();
            $('.onSelect').draggable('enable').resizable('disable').css({
                    "z-index": 2 + arr_id_of_div_box.indexOf($('.onSelect').attr('id'))
                }).removeClass('onSelect').find(".ui-resizable-handle").hide();
            $(this).draggable('enable').resizable('enable')
                .addClass('onSelect').find(".ui-resizable-handle").show();
            
            removeEdit();
            createWholeEditor($div_box);
        }

    });
    
    // textbox 더블클릭 시 텍스트 입력 + 선택 편집 모드로 전환(TODO 텍스트 입력에 따라 높이 자동 조절되도록, 너비는 직접 수정)
    $div_box.dblclick(function(e){

        //이벤트 bubble 제거
        e.stopPropagation();

        removeEdit();

        // 텍스트박스일 경우만 적용
        if ($(this).hasClass('textbox')) {

            // textbox 드래그 끔, 리사이즈 켬, 수정 중 css 적용
            $div_box.draggable('disable').resizable('disable').removeClass('onSelect').addClass('onEdit')
                .find(".ui-resizable-handle").hide();

            // textfiled를 수정 가능하게 변경
            $div_box.prop("contenteditable", true);
        }

    });

    // 마우스 업 이벤트 시 텍스트편집창 띄울 것인지 판단
    $div_box.mouseup(function(e) {

        // 클릭한 대상이...
        if($(this).hasClass('onEdit')) {
            // 수정 중인 박스였다면 부분 옵션창을 켠다.
            removeEdit();
            isTextboxHighlighted();

        } else if ($(this).hasClass('onSelect')) {
            // 선택 중인 박스였다면... 무얼 할까??

        } else {
            // 아무 것도 아닌 박스였다면...?? 

        }

    });

}
// [end] 텍스트 박스를 캔버스에 추가

// 폰트 크기 적용
function execFontSize(size, unit) {
    var $spanString = $('<span />');
    $spanString.html(getSelectionHtml()).css("font-size", size+unit);
    document.execCommand('insertHTML', false, spanString.outerHTML);
};

// 텍스트 선택 시 텍스트 상단에 편집창 팝업
function isTextboxHighlighted(e) {

    var sel = window.getSelection();

    if(sel.rangeCount > 0) {
        var range = sel.getRangeAt(0);
        var clientRects = range.getClientRects();
        if(clientRects.length > 0 && sel.toString().length > 0) {
            createTextEditor(clientRects[0].top, (clientRects[0].right + clientRects[0].left)/2 );
        }
    }
}

// [start] 전역 편집창 생성
function createWholeEditor($elem) {

    var $arr_bt = [];

    //if($elem.hasClass('imagebox')) {창
        
    // 사진 추가
    $arr_bt.push($('<button />'));
    var $i_plus = $('<i class="fas fa-plus"></i>');
    $arr_bt[$arr_bt.length-1].append($i_plus).click(function() {
        //TODO 이미지 추가, 편집
    }).mouseenter(function(e) {
        createTooltip($(this), '추가');
    }).mouseleave(function(e) {
        $('.tooltip').remove();
    });
    
	// 글씨 크기
    $arr_bt.push($('<button />', {
        "id": "bt_size",
        "text": "크기"
    }));
    $arr_bt[$arr_bt.length - 1].click(function() {
        if($('.div_fontsize').css('display') == 'none') {
            $('.div_fontsize').css('display', 'block');
        } else {
            $('.div_fontsize').css('display', 'none');
        }
        
    });
    
	// 글꼴
    $arr_bt.push($('<button />', {
        "id": "bt_font",
        "text": "글꼴"
    }));
    $arr_bt[$arr_bt.length - 1].click(function() {
        if($('.div_font').css('display') == 'none') {
            $('.div_font').css('display', 'block');
        } else {
            $('.div_font').css('display', 'none');
        }
        
    });


    // 삭제
    $arr_bt.push($('<button />'));
    var $i_delete = $('<i class="fas fa-times"></i>');
    $arr_bt[$arr_bt.length-1].append($i_delete).click(function() {
        // TODO 삭제
    }).mouseenter(function(e) {
        createTooltip($(this), '삭제');
    }).mouseleave(function(e) {
        $('.tooltip').remove();
    });

    // 제일 위로
    $arr_bt.push($('<button />'));
    var $i_to_the_top = $('<i class="fas fa-angle-double-up"></i>');
    $arr_bt[$arr_bt.length-1].append($i_to_the_top).click(function() {
        // TODO 제일 위로 올리기

        // id 배열 내 index(z-index는 인덱스 + 2)
        var target_index = arr_id_of_div_box.indexOf($('.onSelect').attr('id'));

        // id 배열 내 onSelect의 id를 가장 뒤로 이동
        arr_id_of_div_box.splice(target_index, 1);
        arr_id_of_div_box.push($('.onSelect').attr('id'));

        // z-index 조정
        for(var i = 0; i < arr_id_of_div_box.length; i++) {
            $('#' + arr_id_of_div_box[i] + '').css({
                "z-index" : 2 + i
            })
        }

    }).mouseenter(function(e) {
        createTooltip($(this), '가장 위로');
    }).mouseleave(function(e) {
        $('.tooltip').remove();
    });

    // 스크롤 보정을 위한 변수
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;

    // 전역 편집창
    var $div_whole_editor = $('<div />');
    $div_whole_editor.addClass('edit').addClass('div_whole_editor').css({
        "position": "absolute",
        "top": $('.onSelect').position().top + $('.pages').position().top + scrollTop + 40,
        "left": $('.onSelect').position().left + $('.pages').position().left + scrollLeft
    }).prop("contenteditable", false);

    // div append
    for(var i = 0; i < $arr_bt.length; i++) {
        var $tmp_div = $('<div />');
        $tmp_div.append($arr_bt[i]);

        // div를 열어야 하는 경우
        if($arr_bt[i].attr('id') == "bt_size") {
            var $innerDiv = $('<div />', {
                "class": "div_fontsize"
            });
            $innerDiv.css({
                "display": "none", 
                "z-index": "1000"
            });
			// 글씨 크기   	
           	var $arr_bt_size = [];
           	// 9pt
            $arr_bt_size.push($('<button />', {
            	"class": "bt_size", 
            	"text": "Cooing(9pt)"
            }));
            $arr_bt_size[$arr_bt_size.length -1].css({
            	"font-size": "9pt"
            }).click(function() {
                $('.onSelect').css("font-size", "9pt");
            });
           	// 10pt
            $arr_bt_size.push($('<button />', {
            	"class": "bt_size", 
            	"text": "Cooing(10pt)"
            }));
            $arr_bt_size[$arr_bt_size.length -1].css({
            	"font-size": "10pt"
            }).click(function() {
                $('.onSelect').css("font-size", "10pt");
            });
           	// 11pt
            $arr_bt_size.push($('<button />', {
            	"class": "bt_size", 
            	"text": "Cooing(11pt)"
            }));
            $arr_bt_size[$arr_bt_size.length -1].css({
            	"font-size": "11pt"
            }).click(function() {
                $('.onSelect').css("font-size", "11pt");
            });
           	// 12pt
            $arr_bt_size.push($('<button />', {
            	"class": "bt_size", 
            	"text": "Cooing(12pt)"
            }));
            $arr_bt_size[$arr_bt_size.length -1].css({
            	"font-size": "12pt"
            }).click(function() {
                $('.onSelect').css("font-size", "12pt");
            });
           	// 14pt
            $arr_bt_size.push($('<button />', {
            	"class": "bt_size", 
            	"text": "Cooing(14pt)"
            }));
            $arr_bt_size[$arr_bt_size.length -1].css({
            	"font-size": "14pt"
            }).click(function() {
                $('.onSelect').css("font-size", "14pt");
            });
           	// 18pt
            $arr_bt_size.push($('<button />', {
            	"class": "bt_size", 
            	"text": "Cooing(18pt)"
            }));
            $arr_bt_size[$arr_bt_size.length -1].css({
            	"font-size": "18pt"
            }).click(function() {
                $('.onSelect').css("font-size", "18pt");
            });
           	// 24pt
            $arr_bt_size.push($('<button />', {
            	"class": "bt_size", 
            	"text": "Cooing(24pt)"
            }));
            $arr_bt_size[$arr_bt_size.length -1].css({
            	"font-size": "24pt"
            }).click(function() {
                $('.onSelect').css("font-size", "24pt");
            });
           	// 36pt
            $arr_bt_size.push($('<button />', {
            	"class": "bt_size", 
            	"text": "Cooing(36pt)"
            }));
            $arr_bt_size[$arr_bt_size.length -1].css({
            	"font-size": "36pt"
            }).click(function() {
                $('.onSelect').css("font-size", "36pt");
            });       
			for (var j = 0; j < $arr_bt_size.length; j++) {
				$innerDiv.append($arr_bt_size[j])
			}
            $tmp_div.append($innerDiv);
        }
        // div를 열어야 하는 경우
        else if($arr_bt[i].attr('id') == "bt_font") {

            var $innerDiv = $('<div />', {
                "class": "div_font"
            });
            $innerDiv.css({
                "display": "none", 
                "z-index": "1000"
            });

            // 글꼴
           	var $arr_bt_font = [];
           	// 궁서체
            $arr_bt_font.push($('<button />', {
            	"class": "bt_font", 
            	"text": "궁서체"
            }));
            $arr_bt_font[$arr_bt_font.length -1].css({
            	"font-family": "궁서체, serif"
            }).click(function() {
                $('.onSelect').css("font-family", "궁서체, serif");
            });
           	// 맑은고딕
            $arr_bt_font.push($('<button />', {
            	"class": "bt_font", 
            	"text": "맑은고딕"
            }));
            $arr_bt_font[$arr_bt_font.length -1].css({
            	"font-family": "Malgun Gothic, serif"
            }).click(function() {
                $('.onSelect').css("font-family", "Malgun Gothic, serif");
            });
           	// 굴림
            $arr_bt_font.push($('<button />', {
            	"class": "bt_font", 
            	"text": "굴림"
            }));
            $arr_bt_font[$arr_bt_font.length -1].css({
            	"font-family": "굴림, serif"
            }).click(function() {
                $('.onSelect').css("font-family", "굴림, serif");
            });
           	// 바탕
            $arr_bt_font.push($('<button />', {
            	"class": "bt_font", 
            	"text": "바탕"
            }));
            $arr_bt_font[$arr_bt_font.length -1].css({
            	"font-family": "바탕, serif"
            }).click(function() {
                $('.onSelect').css("font-family", "바탕, serif");
            });
           	// Nanum Brush Script
            $arr_bt_font.push($('<button />', {
            	"class": "bt_font", 
            	"text": "Nanum Brush Script"
            }));
            $arr_bt_font[$arr_bt_font.length -1].css({
            	"font-family": "Nanum Brush Script, serif"
            }).click(function() {
                $('.onSelect').css("font-family", "Nanum Brush Script, serif");
            });
           	// Gamja Flower
            $arr_bt_font.push($('<button />', {
            	"class": "bt_font", 
            	"text": "Gamja Flower"
            }));
            $arr_bt_font[$arr_bt_font.length -1].css({
            	"font-family": "Gamja Flower, cursive"
            }).click(function() {
                $('.onSelect').css("font-family", "Gamja Flower, cursive");
            }); 
           	// Hi Melody
            $arr_bt_font.push($('<button />', {
            	"class": "bt_font", 
            	"text": "Gugi"
            }));
            $arr_bt_font[$arr_bt_font.length -1].css({
            	"font-family": "Hi Melody, cursive"
            }).click(function() {
                $('.onSelect').css("font-family", "Hi Melody, cursive");
            });
			for (var j = 0; j < $arr_bt_font.length; j++) {
				$innerDiv.append($arr_bt_font[j])
			}
            $tmp_div.append($innerDiv);
        }
        
        $div_whole_editor.append($tmp_div);
    }



    $('body').append($div_whole_editor);

    // $arr_bt[$arr_bt.length-1].parent().mouseenter(function(e) {
    //     createTooltip($(this), '가장 위로');
    // }).mouseleave(function(e) {
    //     $('.div_tooltip').remove();
    // });
    
    // 공통 클래스 적용.....
    $div_whole_editor.contents().addClass('edit');
    $div_whole_editor.contents().contents().addClass('edit');
    
}
// [end] 전역 편집창 생성

// [start] 텍스트 부분 편집창 생성
function createTextEditor(top, left) {

    var $arr_bt = [];
    
    // 왼쪽 정렬
    $arr_bt.push($('<button />'));
    var $i_align_left = $('<i class="fas fa-align-left"></i>');
    $arr_bt[$arr_bt.length-1].append($i_align_left).click(function() {
        document.execCommand('justifyLeft', false, null);
    });

    // 가운데 정렬
    $arr_bt.push($('<button />'));
    var $i_align_center = $('<i class="fas fa-align-center"></i>');
    $arr_bt[$arr_bt.length-1].append($i_align_center).click(function() {
        document.execCommand('justifyCenter', false, null);
    });

    // 오른쪽 정렬
    $arr_bt.push($('<button />'));
    var $i_align_right = $('<i class="fas fa-align-right"></i>');
    $arr_bt[$arr_bt.length-1].append($i_align_right).click(function() {
        document.execCommand('justifyRight', false, null);
    });

    // 볼드
    $arr_bt.push($('<button />'));
    var $i_bold = $('<i class="fas fa-bold"></i>');
    $arr_bt[$arr_bt.length-1].append($i_bold).click(function(e) {
        // TODO 매우 중요!! document.queryCommandState('bold') 
        document.execCommand('bold', false, null);
    });

    // 이탤릭
    $arr_bt.push($('<button />'));
    var $i_italic = $('<i class="fas fa-italic"></i>');
    $arr_bt[$arr_bt.length-1].append($i_italic).click(function(e) {
        // TODO 매우 중요!! document.queryCommandState('bold') 
        document.execCommand('italic', false, null);
    });

    // 밑줄
    $arr_bt.push($('<button />'));
    var $i_underline = $('<i class="fas fa-underline"></i>');
    $arr_bt[$arr_bt.length-1].append($i_underline).click(function(e) {
        // TODO 매우 중요!! document.queryCommandState('bold')
        document.execCommand('underline', false, null);
    });

    // 글자 크게
    $arr_bt.push($('<button />'));
    var $i_font = $('<i class="fas fa-font"></i>');
    var $i_plus = $('<i class="fas fa-plus"></i>');
    $arr_bt[$arr_bt.length-1].append($i_font).append($i_plus).click(function(e) {
        var curr_font_size = document.queryCommandValue('fontSize');
        document.execCommand('fontSize', false, Number(curr_font_size) + 1);
    });

    // 글자 작게
    $arr_bt.push($('<button />'));
    var $i_font = $('<i class="fas fa-font"></i>');
    var $i_minus = $('<i class="fas fa-minus"></i>');
    $arr_bt[$arr_bt.length-1].append($i_font).append($i_minus).click(function(e) {
        var curr_font_size = document.queryCommandValue('fontSize');
        document.execCommand('fontSize', false, Number(curr_font_size) - 1);
    });

    // 글자 색
    $arr_bt.push($('<button />'));
    var $i_font_white = $('<i class="fas fa-font"></i>');
    var $i_font_yellow = $('<i class="fas fa-font"></i>');
    $i_font_yellow.css('color', 'yellow');
    $arr_bt[$arr_bt.length-1].append($i_font_white).append($i_font_yellow).click(function(e) {

        // TODO div 만들고 div 내에 색상 선택 옵션 보여주어야 함.

        // 글자 색 바꾸는 예제
        document.execCommand('foreColor', false, 'red');
    });

    // // [start] TODO 폰트 크기
    // var $div_fontsize = $('<div />');
    // var bt_curr_fontsize = document.createElement('button');
    // var div_select_fontsize = document.createElement('div');
    // $(div_select_fontsize).css({
    //     "display": "none"
    // });
    // var arr_fontsize = ['9', '10', '11', '12', '14', '18', '24', '36', '60'];
    // var arr_bt_fontsize = [];
    // for (var i = 0; i < arr_fontsize.length; i++) {
    //     arr_bt_fontsize[i] = document.createElement('button');
    //     $(arr_bt_fontsize[i]).text(arr_fontsize[i]).click(function() {
    //         execFontSize($(this).text(), "px");
    //     }).appendTo(div_select_fontsize);
    // }

    // if(document.queryCommandState('fontSize') == false) {
    //     $(bt_curr_fontsize).text('16');
    // } else {
    //     $(bt_curr_fontsize).text(document.queryCommandState('fontSize'));
    // }
    // $(bt_curr_fontsize).click(function(e) {
    //     // TODO 매우 중요!! document.queryCommandState('bold')
    //     if($(div_select_fontsize).css("display") == "block") {
    //         $(div_select_fontsize).css("display", "none");
    //     } else {
    //         $(div_select_fontsize).css("display", "block");
    //     }
    // });

    // $div_fontsize.append(bt_curr_fontsize).append(div_select_fontsize);
    // // [end] TODO 폰트 크기

    // 스크롤 보정을 위한 변수
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;

    // 텍스트 편집 옵션창
    var $div_selection_editor = $('<div />');
    $div_selection_editor.addClass('edit').addClass('div_selection_editor').css({
        "position": "absolute",
        "top": top + scrollTop - 65,
        "left": left + scrollLeft - 225
    }).prop("contenteditable", false);

    // 효과 버튼 append
    for(var i = 0; i < $arr_bt.length; i++) {
        var $tmp_div = $('<div />');
        $tmp_div.append($arr_bt[i]);
        $div_selection_editor.append($tmp_div);
    }

    $('body').append($div_selection_editor);
    
    // 공통 클래스 적용.....
    $div_selection_editor.contents().addClass('edit');
    $div_selection_editor.contents().contents().addClass('edit');
}
// [end] 텍스트 부분 편집창 생성

// 편집창 제거
function removeEdit() {
    $('.div_selection_editor').remove();
    $('.div_whole_editor').remove();
}

// [start] 도움말 생성
function createTooltip($elem, text) {

    // 스크롤 보정을 위한 변수
    var scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
    var scrollLeft = document.documentElement.scrollLeft || document.body.scrollLeft;

    var $editor = $('.div_whole_editor') || $('.div_selection_editor');

    // 도움말
    var $div_tooltip = $('<div />');
    $div_tooltip.addClass('tooltip').html(text);

    $('body').append($div_tooltip);

    $div_tooltip.css({
        "top": $editor.position().top + scrollTop + 45,
        "left": $editor.position().left + $elem.parent().position().left + 20 + scrollLeft 
            - $div_tooltip.width()/2
    })

}
// [end] 도움말 생성

function save_div() {
    var target = document.getElementById('box_0');
    $(target).draggable('destroy').resizable('destroy');
    var clone = $(target.outerHTML);
    // clone.css('background-color', 'blue').draggable({
    //     // textbox 드래그 시 위치 이동 처리 (ui.helper는 이벤트의 대상)
    //     stop: function(event, ui) {
    //         var id = ui.helper.attr("id");
    //         var box = map_box.get(id);
    //         box.position.top = ui.position.top;
    //         box.position.left = ui.position.left;
    //     },
    //     containment: '.page-wrapper'  // 캔버스 영역 밖으로 나가지 못하게 제한
    // });
    $(target).remove();
    renderbox(clone);
    //$('.page-wrapper').append(clone);
    //alert(target.outerHTML);
    //$('#map_box').text();
}